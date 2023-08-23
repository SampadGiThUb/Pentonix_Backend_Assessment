def length_of_longest_substring(s):
    index = {}
    length = 0
    start = 0
    for end in range(len(s)):
        if s[end] in index and index[s[end]] >= start:
            start = index[s[end]] + 1
        index[s[end]] = end
        length = max(length, end - start + 1)
    return length


s = input("s: ")
result = length_of_longest_substring(s)
print("output:", result)
