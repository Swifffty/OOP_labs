#ifndef LAB1_BITARRAY_H
#define LAB1_BITARRAY_H

#include <vector>
#include <limits>
#include <string>
#include <cmath>

namespace bitarray {
    class BitArray {
    private:
        std::vector<unsigned long> array_of_bits;
        int size_array;
        const int bits_of_long = std::numeric_limits<unsigned long>::digits;
        const unsigned long max_long = std::numeric_limits<unsigned long>::max();
        void bit_change_left(int delete_full_elements_vector, int n);
        void bit_change_right(int delete_full_elements_vector, int n);

    public:
        BitArray();

        ~BitArray() = default;

        explicit BitArray(int num_bits, unsigned long value = 0);

        BitArray(const BitArray &b);

        void swap(BitArray &b);

        BitArray &operator=(const BitArray &b);

        void resize(int num_bits, bool value = false);

        void clear();

        void push_back(bool bit);

        bool operator[](int i) const;

        BitArray &operator&=(const BitArray &b);

        BitArray &operator|=(const BitArray &b);

        BitArray &operator^=(const BitArray &b);

        BitArray &operator<<=(int n);

        BitArray &operator>>=(int n);

        BitArray operator<<(int n) const;

        BitArray operator>>(int n) const;

        BitArray &set(int n, bool val = true);

        BitArray &set();

        BitArray &reset(int n);

        BitArray &reset();

        bool any() const;

        bool none() const;

        BitArray operator~() const;

        int count() const;

        int size() const;

        bool empty() const;

        std::string to_string() const;
    };

    bool operator==(const BitArray& b1, const BitArray& b2);

    bool operator!=(const BitArray& b1, const BitArray& b2);

    BitArray operator&(const BitArray& b1, const BitArray& b2);

    BitArray operator|(const BitArray& b1, const BitArray& b2);

    BitArray operator^(const BitArray& b1, const BitArray& b2);

}

using namespace bitarray;


#endif //LAB1_BITARRAY_H
