#include "WavWriter.h"

WavWriter::WavWriter(const std::string& filename) {
    file_.open(filename, std::ios::binary | std::ios::trunc);
    if (!file_.is_open()) {
        throw FileWriteException("Не удалось создать файл: " + filename);
    }
    
    // Записываем 'заглушку' заголовка (58 байт)
    RiffChunk riff;
    FmtChunk fmt;
    DataChunk data;
    
    file_.write(reinterpret_cast<const char*>(&riff), sizeof(RiffChunk));
    file_.write(reinterpret_cast<const char*>(&fmt), sizeof(FmtChunk));
    file_.write(reinterpret_cast<const char*>(&data), sizeof(DataChunk));
    
    // Файловый указатель теперь стоит на начале блока данных.
}

WavWriter::~WavWriter() {
    // В деструкторе можно добавить логику, чтобы убедиться, что заголовок записан
    if (file_.is_open()) {
        finalizeHeader();
        file_.close();
    }
}

void WavWriter::writeSample(int16_t sample) {
    if (!file_.is_open()) return;
    
    file_.write(reinterpret_cast<const char*>(&sample), sizeof(int16_t));
    dataSize_ += sizeof(int16_t);
}

void WavWriter::finalizeHeader() {
    if (!file_.is_open()) return;

    RiffChunk riff;
    DataChunk data;
    
    // 1. Обновляем DataChunk: размер данных
    data.chunkSize = (uint32_t)dataSize_;
    
    // Возвращаемся в начало Data-чанка (RIFF(12) + FMT(24) = 36)
    file_.seekp(sizeof(RiffChunk) + sizeof(FmtChunk), std::ios_base::beg);
    file_.write(reinterpret_cast<const char*>(&data), sizeof(DataChunk));
    
    // 2. Обновляем RIFFChunk: общий размер файла
    riff.chunkSize = (uint32_t)(dataSize_ + sizeof(FmtChunk) + sizeof(DataChunk) + 4);
    
    // Возвращаемся в начало файла
    file_.seekp(4, std::ios_base::beg); // Пропускаем первые 4 байта ("RIFF")
    file_.write(reinterpret_cast<const char*>(&riff.chunkSize), sizeof(uint32_t));
    
    // Возвращаемся в конец, чтобы избежать усечения
    file_.seekp(0, std::ios_base::end);
}