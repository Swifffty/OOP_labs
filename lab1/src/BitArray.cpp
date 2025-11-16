#include "BitArray.h"

BitArray::BitArray() {
    array_of_bits.push_back(0);
    size_array = -1; // сделать минимальнный массив
}


int bits_needed_unsigned(unsigned int n) {
    if (n == 0) return 1;
    return static_cast<int>(std::log2(n)) + 1;
}

unsigned long reverse_first_bits(unsigned long &value, unsigned int const &bits_of_long, int const &num_bits) {
    unsigned long mask_for_new_value = 1 << (bits_of_long - 1);
    unsigned long mask_for_old_value = 1;
    unsigned long new_value = 0;

    while (mask_for_new_value != 0) {
        if (value & mask_for_old_value) {
            new_value |= mask_for_new_value;
        }
        mask_for_new_value >>= 1;
        mask_for_old_value <<= 1;
    }
    int num_bits_for_val = bits_needed_unsigned(value);
    if (num_bits_for_val < bits_of_long) {
        new_value >>= (bits_of_long - num_bits_for_val);
    }
    return new_value;
}

BitArray::BitArray(int num_bits, unsigned long value) {
    if (value) {
        value = reverse_first_bits(value, bits_of_long, num_bits);
    }
    array_of_bits.push_back(value);
    size_array = --num_bits;

    int count_iterations = num_bits / bits_of_long;
    if (count_iterations > 0) {
        for (int i = 1; i <= count_iterations; i++) {
             array_of_bits.push_back(0);
        }
    }
}

bool BitArray:: operator[](int i) const{

    if (i < 0 || i > size_array) {
        throw std::string{"bad index"};
    }

    unsigned int index_of_vector = i / bits_of_long;
    unsigned int index_in_word = i % bits_of_long;

    unsigned long mask_for_bit = 1 << index_in_word;

    return (array_of_bits[index_of_vector] & mask_for_bit); // лишняя проверка
}

BitArray &BitArray::operator=(const BitArray &b) {
    if (this != &b) {
        array_of_bits = b.array_of_bits;
        size_array = b.size_array;
    }
    return *this;
}

BitArray::BitArray(const BitArray &b) {
    operator=(b);
}

void BitArray::swap(BitArray &b) {
    std::vector<unsigned long> tmp_array = array_of_bits;
    int tmp_size = size_array;
    operator=(b);
    b.array_of_bits = tmp_array;
    b.size_array = tmp_size;
}

void BitArray::resize(int num_bits, bool value) {
    if (num_bits < 0) {
        throw std::string{"bad num bits in resize"};
    }
    num_bits--;
    if (num_bits == size_array) {
        return;
    }
    int index_of_vector = num_bits / bits_of_long;
    int index_of_vector_bits = size_array / bits_of_long;

    if (size_array < num_bits) {

        int bit_in_word_bits = size_array % bits_of_long;
        unsigned long mask_for_bits = max_long;

        if (value) {
            mask_for_bits <<= (bit_in_word_bits + 1);
            array_of_bits[index_of_vector_bits] |= mask_for_bits;
            for (int i = index_of_vector_bits + 1; i <=  index_of_vector; i++) {
                array_of_bits[i] = max_long;
            }

        } else {
            mask_for_bits >>= (bits_of_long - bit_in_word_bits - 1);
            array_of_bits[index_of_vector_bits] &= mask_for_bits;
            for (int i = index_of_vector_bits + 1; i <= index_of_vector; i++) {
                array_of_bits[i] = 0;
            }
        }
    } else {
        while (index_of_vector_bits != index_of_vector) {
            array_of_bits.pop_back();
            index_of_vector_bits--;
        }
    }
    size_array = num_bits;
}

void BitArray::clear() {
    array_of_bits.clear();
    size_array = -1;
}

void BitArray::push_back(bool bit) {
    int new_position = ++size_array;
    int index_of_vector = new_position / bits_of_long;
    int position_in_word = new_position % bits_of_long;

    if (bit) {
        unsigned long mask = 1 << position_in_word;
        array_of_bits[index_of_vector] |= mask;
    } else {
        unsigned long mask = max_long >> (bits_of_long - position_in_word);
        array_of_bits[index_of_vector] &= mask;
    }
}

BitArray& BitArray::operator&=(const BitArray &b) {
    int size_vector = size_array / bits_of_long;
    for (int i = 0; i <= size_vector; i++) {
        array_of_bits[i] &= b.array_of_bits[i];
    }
    return *this;
}

BitArray& BitArray::operator|=(const BitArray &b) {
    int size_vector = size_array / bits_of_long;
    for (int i = 0; i <= size_vector; i++) {
        array_of_bits[i] |= b.array_of_bits[i];
    }
    return *this;
}

BitArray& BitArray::operator^=(const BitArray &b) {
    int size_vector = size_array / bits_of_long;
    for (int i = 0; i <= size_vector; i++) {
        array_of_bits[i] ^= b.array_of_bits[i];
    }
    return *this;
}

void BitArray::bit_change_left(int delete_full_elements_vector, int n) {
    for (int i = 0; i < (size_array / bits_of_long) - delete_full_elements_vector; i++) {
        array_of_bits[i] >>= n;
        unsigned long mask = ((max_long >> (bits_of_long - n)) & array_of_bits[i + 1]) << (bits_of_long - n);
        array_of_bits[i] |= mask;
    }
    array_of_bits[size_array / bits_of_long - delete_full_elements_vector] >>= n;
    unsigned long mask = max_long >> (bits_of_long - (size_array % bits_of_long) - 1);
    array_of_bits[size_array / bits_of_long] &= mask;

    for (int i = (size_array / bits_of_long) - delete_full_elements_vector + 1; i <= size_array / bits_of_long; i++) {
        array_of_bits[i] = 0;
    }
}

BitArray& BitArray::operator<<=(int n) {
    if (n == 0 || size_array == -1) {
        return *this;
    }
    if (n > size_array + 1) {
        for (int i = 0; i <= ((n - 1) / bits_of_long); i++) {
            array_of_bits[i] = 0;
        }
        return *this;
    }
        int delete_full_elements_vector = n / bits_of_long;
        for (int i = delete_full_elements_vector; i <= (size_array / bits_of_long); i++) {
            array_of_bits[i - delete_full_elements_vector] = array_of_bits[i];
        }
        n %= bits_of_long;

        if (n == 0) {
            return *this;
        } else {
            bit_change_left(delete_full_elements_vector, n);
        }
    return *this;
}

void BitArray::bit_change_right(int delete_full_elements_vector, int n) {
    for (int i = size_array / bits_of_long; i > delete_full_elements_vector; i--) {
        array_of_bits[i] <<= n;
        unsigned long mask = (max_long << (bits_of_long - n)) & array_of_bits[i - 1];
        array_of_bits[i] |= (mask >> (bits_of_long - n));
    }
    array_of_bits[delete_full_elements_vector] <<= n;
    unsigned long mask = max_long >> (bits_of_long - (size_array % bits_of_long) - 1);
    array_of_bits[size_array / bits_of_long] &= mask;


    for (int i = delete_full_elements_vector - 1; i >= 0 ; i--) {
        array_of_bits[i] = 0;
    }
}

BitArray& BitArray::operator>>=(int n) {
    if (n == 0 || size_array == -1) {
        return *this;
    }
    if (n > size_array + 1) {
        for (int i = 0; i <= ((n - 1) / bits_of_long); i++) {
            array_of_bits[i] = 0;
        }
        return *this;
    }
        int delete_full_elements_vector = n / bits_of_long;
        for (int i = (size_array / bits_of_long); i >= delete_full_elements_vector; i--) {
            array_of_bits[i] = array_of_bits[i - delete_full_elements_vector];
        }
        n %= bits_of_long;

        if (n == 0) {
            return *this;
        } else {
            bit_change_right(delete_full_elements_vector, n);
        }
    return *this;
}

BitArray BitArray::operator<<(int n) const {
    BitArray b(*this);

    b.operator<<=(n);
    return b;
}

BitArray BitArray::operator>>(int n) const {
    BitArray b(*this);

    b.operator>>=(n);
    return b;
}

BitArray& BitArray::set(int n, bool val) {

    if (n < 0 || n > size_array) {
        throw std::string{"bad index in set"};
    }

    if (operator[](n) == val) {
        return *this;
    }

    int vector_index = n / bits_of_long;
    int word_index = n % bits_of_long;


    if (val) {
        unsigned long mask = 1;
        if (word_index == 0) {
            array_of_bits[vector_index] |= mask;
            return *this;
        }

        if (word_index == (bits_of_long - 1)) {
            mask <<= (bits_of_long - 1);
            array_of_bits[vector_index] |= mask;
            return *this;
        }

        mask <<= word_index;
        array_of_bits[vector_index] |= mask;
    } else {
        unsigned long mask = max_long;

        if (word_index == 0) {
            mask <<= 1;
            array_of_bits[vector_index] &= mask;
            return *this;
        }

        if (word_index == bits_of_long - 1) {
            mask >>= 1;
            array_of_bits[vector_index] &= mask;
            return *this;
        }

        unsigned long first_part_mask = max_long << (word_index + 1);
        unsigned long second_part_mask = max_long >> (bits_of_long - word_index);
        mask = first_part_mask | second_part_mask;
        array_of_bits[vector_index] &= mask;
    }
    return *this;
}

BitArray& BitArray::set() {
    for (int i = 0; i <= (size_array / bits_of_long); i++) {
        array_of_bits[i] = max_long;
    }
    return *this;
}

BitArray& BitArray::reset(int n) {
    return set(n, false);
}

BitArray& BitArray::reset() {
    for (int i = 0; i <= (size_array / bits_of_long); i++) {
        array_of_bits[i] = 0;
    }
    return *this;
}

bool BitArray::any() const{
    for (int i = 0; i <= (size_array / bits_of_long); i++) {
        if (array_of_bits[i] != 0) {
            return true;
        }
    }
    return false;
}

bool BitArray::none() const{
    return(!any());
}

BitArray BitArray::operator~() const {
    BitArray b(*this);

    for (int i = 0; i <= size_array; i++) {
        bool value = !operator[](i);
        b.set(i, value);
    }
    return b;
}

int BitArray::count() const {
    int cnt = 0;
    for (int i = 0; i <= size_array; i++) {
        if (operator[](i)) {
            cnt++;
        }
    }
    return cnt;
}

int BitArray::size() const {
    return (size_array + 1);
}

bool BitArray::empty() const {
    if (size_array == -1) {
        return true;
    } else {
        return false;
    }
}

std::string BitArray::to_string() const {
    std::string result_bit = std::to_string(operator[](0));

    for (int i = 1; i <= size_array; i++) {
        result_bit += std::to_string(operator[](i));
    }
    return result_bit;
}

bool bitarray::operator==(const BitArray& b1, const BitArray& b2) {
    if (b1.size() != b2.size()) {
        return false;
    }
    for (int i = 0; i < b1.size(); i++) {
        if (b1.operator[](i) != b2.operator[](i)) {
            return false;
        }
    }
    return true;
}

bool bitarray::operator!=(const BitArray& b1, const BitArray& b2) {
    return !(operator==(b1, b2));
}

BitArray bitarray::operator&(const BitArray& b1, const BitArray& b2) {
    BitArray c(b1);
    c.operator&=(b2);
    return c;
}

BitArray bitarray::operator|(const BitArray& b1, const BitArray& b2) {
    BitArray c(b1);
    c.operator|=(b2);
    return c;
}

BitArray bitarray::operator^(const BitArray& b1, const BitArray& b2) {
    BitArray c(b1);
    c.operator^=(b2);
    return c;
}
