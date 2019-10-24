#include "bloom.h"

const int BLOOM_HASH1 = 17;
const int BLOOM_HASH2 = 29;

// compute a hash of a string using a seed value, where the result
// falls between zero and range-1
int hash_string(char * string, int seed, int range)
{
  int i;
  int hash = 0;

  // simple loop for mixing the input string
  for ( i = 0; string[i] != '\0'; i++ ) {
    hash = hash * seed + string[i];
  }
  // check for unlikely case that hash is negative
  if ( hash < 0 ) {
    hash = -hash;
  }
  // bring the hash within the range 0..range-1
  hash = hash % range;

  return hash;
}


// create a new, empty Bloom filter of 'size' items
struct bloom * bloom_new(int size) {
	struct bloom * result;
	result = malloc(sizeof(struct bloom));
	struct bitset * set = bitset_new(size);
	result -> size = size;
	result -> bitset = set;
	return result;
}

// check to see if a string is in the set
int bloom_lookup(struct bloom * this, char * item) {
	struct bitset * set = this -> bitset;
	int hash1 = hash_string(item, BLOOM_HASH1, this -> size);
	int hash2 = hash_string(item, BLOOM_HASH2, this -> size);
	return (bitset_lookup(set, hash1) == 1 || bitset_lookup(set, hash2) == 1)?1:0;
}

// add a string to the set
// has no effect if the item is already in the set
void bloom_add(struct bloom * this, char * item) {
	struct bitset * set = this -> bitset;
	int hash1 = hash_string(item, BLOOM_HASH1, this -> size);
	int hash2 = hash_string(item, BLOOM_HASH2, this -> size);
	bitset_add(set, hash1);
	bitset_add(set, hash2);
}

// place the union of src1 and src2 into dest
void bloom_union(struct bloom * dest, struct bloom * src1,
    struct bloom * src2) {
	struct bitset * set1 = src1 -> bitset;
	struct bitset * set2 = src2 -> bitset;
	struct bitset * set3 = dest -> bitset;
	bitset_union(set3, set1, set2);
}


// place the intersection of src1 and src2 into dest
void bloom_intersect(struct bloom * dest, struct bloom * src1,
    struct bloom * src2) {
	struct bitset * set1 = src1 -> bitset;
	struct bitset * set2 = src2 -> bitset;
	struct bitset * set3 = dest -> bitset;
	bitset_intersect(set3, set1, set2);
}
