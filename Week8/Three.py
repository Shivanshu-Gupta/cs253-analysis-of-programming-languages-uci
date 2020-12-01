import sys, string
import numpy as np

# Example input: "Hello  World!"
chars = np.array([' ']+list(open(sys.argv[1]).read())+[' '], dtype='U3')
# Result: array([' ', 'H', 'e', 'l', 'l', 'o', ' ', ' ',
#           'W', 'o', 'r', 'l', 'd', '!', ' '], dtype='s'<U1')

# Normalize
chars[~np.char.isalpha(chars)] = ' '
chars = np.char.lower(chars)
# Result: array([' ', 'h', 'e', 'l', 'l', 'o', ' ', ' ',
#           'w', 'o', 'r', 'l', 'd', ' ', ' '], dtype='<U1')

leet_dict = {
    'a': 'a',
    'b': '8',
    'c': '<',
    'd': '|)',
    'e': '3',
    'f': '|=',
    'g': '[',
    'h': '#',
    'i': '!',
    'j': '_|',
    'k': '|<',
    'l': '|',
    'm': '|\/|',
    'n': '|\|',
    'o': '0',
    'p': '|o',
    'q': 'O_',
    'r': '|2',
    's': '5',
    't': '7',
    'u': '|_|',
    'v': '\/',
    'w': '|/\|',
    'x': '%',
    'y': '`/',
    'z': '2',
    ' ': ' '
}

### Split the words by finding the indices of spaces
sp = np.where(chars == ' ')
# Result: (array([ 0, 6, 7, 13, 14], dtype=int64),)
# A little trick: let's double each index, and then take pairs
sp2 = np.repeat(sp, 2)
# Result: array([ 0, 0, 6, 6, 7, 7, 13, 13, 14, 14], dtype=int64)
# Get the pairs as a 2D matrix, skip the first and the last
w_ranges = np.reshape(sp2[1:-1], (-1, 2))
# Result: array([[ 0,  6],
#                [ 6,  7],
#                [ 7, 13],
#                [13, 14]], dtype=int64)
# Remove the indexing to the spaces themselves
w_ranges = w_ranges[np.where(w_ranges[:, 1] - w_ranges[:, 0] > 2)]
# Result: array([[ 0,  6],
#                [ 7, 13]], dtype=int64)

# Voila! Words are in between spaces, given as pairs of indices
words = list(map(lambda r: chars[r[0]:r[1]], w_ranges))
# Result: [array([' ', 'h', 'e', 'l', 'l', 'o'], dtype='<U1'),
#          array([' ', 'w', 'o', 'r', 'l', 'd'], dtype='<U1')]
# Let's recode the characters as strings
swords = np.array(list(map(lambda w: [''.join(w).strip(), ''.join([leet_dict[c] for c in w]).strip()], words)))
# Result: array([['hello', '#3||0'],
#                ['world', '|/\\|0|2||)']], dtype='<U11')

# Next, let's remove stop words
stop_words = np.array(list(set(open('../stop_words.txt').read().split(','))))
ns_words = swords[~np.isin(swords[:, 0], stop_words)]
ns_words = ns_words[:, 1]

bi_grams = np.array(list(map(' '.join, zip(ns_words[:-1], ns_words[1:]))))

### Finally, count the word occurrences
uniq, counts = np.unique(bi_grams, axis=0, return_counts=True)
wf_sorted = sorted(zip(uniq, counts), key=lambda t: t[1], reverse=True)

for w, c in wf_sorted[:5]:
    print(w, '-', c)
# mr darcy 273
# mrs bennet 153
# mr collins 150
# lady catherine 116
# mr bingley 115
