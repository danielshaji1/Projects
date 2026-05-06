#!/usr/bin/env python3
import sys
import os
sys.path.insert(0, '/Users/danielshaji/Downloads/Cryptography Projects')

from padding import pad, encrypt_cbc, decrypt_cbc, check_pad, AES, xor_bytes

# Test parameters
key = os.urandom(16)
iv = os.urandom(16)
plaintext = b'A' * 15  # 15 bytes, will be padded with 0x01

print("=" * 70)
print("DEBUG: Padding Oracle Attack - Last Byte Recovery")
print("=" * 70)

print(f"\n1. Original plaintext: {plaintext} (length: {len(plaintext)})")

# Encrypt
print("\n2. Encrypting...")
ciphertext = encrypt_cbc(plaintext, key, iv)
print(f"   Ciphertext length: {len(ciphertext)} bytes")
print(f"   IV (C0): {iv.hex()}")

# Manually test the oracle with different IV values
print("\n3. Testing oracle with different IV last-byte values...")
C0 = iv
C1 = ciphertext[:16]

# First, test the original IV to verify it works
print(f"\n   Testing ORIGINAL IV last byte ({hex(C0[-1])})...")
result = decrypt_cbc(C1, key, C0)
if result is not None:
    print(f"     ✓ Valid padding! Result length: {len(result)}, result: {result}")
else:
    print(f"     ✗ Invalid padding")

# Now test a few other guesses
print(f"\n   Testing OTHER IV values...")
valid_count = 0
valid_guesses = []
for guess in range(256):
    C0_modified = C0[:-1] + bytes([guess])

    if C0_modified == C0:
        print(f"     Skipping guess {hex(guess)} (same as original)")
        continue

    result = decrypt_cbc(C1, key, C0_modified)

    if result is not None:
        print(f"     ✓ Valid padding with guess {hex(guess)}!")
        valid_count += 1
        valid_guesses.append((guess, result))
        if valid_count >= 3:  # Only show first 3
            break

if valid_count == 0:
    print("     NO valid guesses found! This is a problem.")
else:
    print(f"\n   Found {valid_count} valid guess(es)")
    for g, r in valid_guesses:
        print(f"     Guess {hex(g)}: result length {len(r)}")

# Manually decrypt to understand
print("\n4. Manual decryption analysis...")
aes = AES(key)
dec_block = aes.decrypt(C1)
print(f"   AES decrypted block (last 4 bytes): {dec_block[-4:].hex()}")
print(f"   IV (last 4 bytes):                  {C0[-4:].hex()}")

# Calculate what the last byte should be
dec_last = dec_block[-1]
iv_last = C0[-1]
plaintext_last = dec_last ^ iv_last
print(f"   Calculated plaintext[-1]:          {hex(plaintext_last)}")
print(f"   Actual plaintext[-1]:              {hex(plaintext[-1])} (0x41 = 'A')")

# The decrypted + padding
padded_plaintext = pad(plaintext)
print(f"   Padded plaintext (last byte):      {hex(padded_plaintext[-1])}")
print(f"   check_pad result:                  {check_pad(padded_plaintext)}")
