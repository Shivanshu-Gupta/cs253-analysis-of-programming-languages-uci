#!/usr/bin/env python
import re, sys, operator

# Mileage may vary. If this crashes, make it lower
RECURSION_LIMIT = 7000
# We add a few more, because, contrary to the name,
# this doesn't just rule recursion: it rules the
# depth of the call stack
sys.setrecursionlimit(RECURSION_LIMIT+10)

def count(word_list, stopwords, wordfreqs):
    # What to do with an empty list
    if word_list == []:
        return
    # The inductive case, what to do with a list of words
    else:
        # Process the head word
        word = word_list[0]
        if word not in stopwords:
            if word in wordfreqs:
                wordfreqs[word] += 1
            else:
                wordfreqs[word] = 1
        # Process the tail
        count(word_list[1:], stopwords, wordfreqs)

def wf_print(wordfreq):
    if wordfreq == []:
        return
    else:
        (w, c) = wordfreq[0]
        print(w, '-', c)
        wf_print(wordfreq[1:])

stop_words = set(open('../stop_words.txt').read().split(','))
# words = re.findall('[a-z]{2,}', open(sys.argv[1]).read().lower())
words = re.findall('[a-z]{2,}', open('../pride-and-prejudice.txt').read().lower())
word_freqs = {}
# Theoretically, we would just call count(words, stop_words, word_freqs)
# Try doing that and see what happens.
for i in range(0, len(words), RECURSION_LIMIT):
    count(words[i:i+RECURSION_LIMIT], stop_words, word_freqs)

def binary_search(l, x, pred, lo, hi):
    if lo == hi:
        return lo
    else:
        mid = lo + ((hi - lo) >> 1)
        if not pred(mid):
            return binary_search(l, x, pred, mid + 1, hi)
        else:
            return binary_search(l, x, pred, lo, mid)

def search(l, x, key=lambda x: x, lo=0, hi=None):
    if hi is None:
        hi = len(l)
    pred = lambda i: key(l[i]) >= key(x)
    pos = binary_search(l, x, pred, lo, hi)
    return pos

def sort_recursive(l, key=lambda x: x, sorted_l = []):
    if len(sorted_l) < len(l):
        x = l[len(sorted_l)]
        pos = search(sorted_l, x, key)
        sorted_l.insert(pos, x)
        sort_recursive(l, key, sorted_l)
    return sorted_l

def sort(iterable, key=lambda x: x, reverse=False):
    l = list(iterable)
    sorted_l = sort_recursive(l, key)
    if reverse:
        sorted_l.reverse()
    return sorted_l

# wf_print(sorted(word_freqs.items(), key=operator.itemgetter(1), reverse=True)[:25])
wf_print(sort(word_freqs.items(), key=operator.itemgetter(1), reverse=True)[:25])

# mr - 786
# elizabeth - 635
# very - 488
# darcy - 418
# such - 395
# mrs - 343
# much - 329
# more - 327
# bennet - 323
# bingley - 306
# jane - 295
# miss - 283
# one - 275
# know - 239
# before - 229
# herself - 227
# though - 226
# well - 224
# never - 220
# sister - 218
# soon - 216
# think - 211
# now - 209
# time - 203
# good - 201