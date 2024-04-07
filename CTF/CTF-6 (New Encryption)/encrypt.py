import string

START = ord("a")
CHARSET = string.ascii_lowercase[:16]

def encode_b16(plain):
	encoded = ""
	for c in plain:
		binary = "{0:08b}".format(ord(c))
		encoded += (CHARSET[int(binary[:4], 2)] + CHARSET[int(binary[4:], 2)])
	return encoded

def caesar_shift(c, k):
	return CHARSET[(ord(c) + ord(k) - 2 * START) % len(CHARSET)]



flag = "secretkey"
# hint: key is a single letter
key = "secretkey"

b16 = encode_b16(flag)
enc = ""
for i, c in enumerate(b16):
	enc += caesar_shift(c, key[i % len(key)])
print(enc)