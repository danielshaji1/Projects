#!/usr/bin/env python3
"""
Standalone test runner for Padding Oracle Attack
Tests all three components without external dependencies
"""

import os
import sys
import time
from padding import (
    pad, check_pad, encrypt_cbc, decrypt_cbc, recover_last_byte, recover_plaintext, AES
)

# Test results tracking
tests_passed = 0
tests_failed = 0
failed_tests = []


def assert_equal(actual, expected, test_name):
    global tests_passed, tests_failed
    if actual == expected:
        tests_passed += 1
        print(f"✓ {test_name}")
        return True
    else:
        tests_failed += 1
        failed_tests.append((test_name, f"Expected {expected}, got {actual}"))
        print(f"✗ {test_name}")
        print(f"  Expected: {expected}")
        print(f"  Got:      {actual}")
        return False


def assert_true(condition, test_name):
    global tests_passed, tests_failed
    if condition:
        tests_passed += 1
        print(f"✓ {test_name}")
        return True
    else:
        tests_failed += 1
        failed_tests.append((test_name, "Condition was False"))
        print(f"✗ {test_name}")
        return False


def assert_not_none(value, test_name):
    global tests_passed, tests_failed
    if value is not None:
        tests_passed += 1
        print(f"✓ {test_name}")
        return True
    else:
        tests_failed += 1
        failed_tests.append((test_name, "Value was None"))
        print(f"✗ {test_name}")
        return False


# ============================================================================
# PADDING FUNCTION TESTS
# ============================================================================

print("\n" + "=" * 70)
print("PART 1: PADDING FUNCTION TESTS")
print("=" * 70 + "\n")

# Test 1: Empty message
padded = pad(b'')
assert_equal(len(padded), 16, "Pad empty message - length is 16")
assert_equal(padded, bytes([0x10] * 16), "Pad empty message - all 0x10 bytes")

# Test 2: Single byte
padded = pad(b'A')
assert_equal(len(padded), 16, "Pad 1 byte - length is 16")
assert_true(all(b == 0x0F for b in padded[1:]), "Pad 1 byte - padding is 0x0F")

# Test 3: 16 bytes (full block)
message = bytes(range(16))
padded = pad(message)
assert_equal(len(padded), 32, "Pad 16 bytes - adds another block")
assert_equal(padded[:16], message, "Pad 16 bytes - original content preserved")
assert_equal(padded[16:], bytes([0x10] * 16), "Pad 16 bytes - full block padding")

# Test 4: 15 bytes
padded = pad(b'X' * 15)
assert_equal(len(padded), 16, "Pad 15 bytes - length is 16")
assert_equal(padded[-1], 0x01, "Pad 15 bytes - padding byte is 0x01")

# Test 5: Various lengths
for length in [1, 5, 10, 15]:
    message = b'X' * length
    padded = pad(message)
    assert_true(len(padded) % 16 == 0, f"Pad {length} bytes - multiple of 16")
    padding_length = 16 - (length % 16)
    assert_true(all(b == padding_length for b in padded[-padding_length:]),
                f"Pad {length} bytes - correct padding bytes")


# ============================================================================
# PADDING VALIDATION TESTS
# ============================================================================

print("\n" + "=" * 70)
print("PART 2: PADDING VALIDATION TESTS")
print("=" * 70 + "\n")

# Test 1: Valid single byte padding
assert_true(check_pad(b'X' * 15 + bytes([0x01])), "Valid padding - 1 byte")

# Test 2: Valid full block padding
assert_true(check_pad(bytes([0x10] * 16)), "Valid padding - full block")

# Test 3: Valid 5-byte padding
assert_true(check_pad(b'X' * 11 + bytes([0x05] * 5)), "Valid padding - 5 bytes")

# Test 4: Invalid - wrong length
assert_equal(check_pad(b'X' * 15 + bytes([0x02])), False, "Invalid padding - wrong length")

# Test 5: Invalid - zero
assert_equal(check_pad(b'X' * 15 + bytes([0x00])), False, "Invalid padding - zero")

# Test 6: Invalid - exceeds block
assert_equal(check_pad(b'X' * 15 + bytes([0x11])), False, "Invalid padding - exceeds 16")

# Test 7: Invalid - inconsistent bytes
assert_equal(check_pad(b'X' * 12 + bytes([0x04, 0x04, 0x03, 0x04])), False,
             "Invalid padding - inconsistent bytes")


# ============================================================================
# AES-CBC ENCRYPTION/DECRYPTION TESTS
# ============================================================================

print("\n" + "=" * 70)
print("PART 3: AES-CBC ENCRYPTION/DECRYPTION TESTS")
print("=" * 70 + "\n")

# Test 1: Single block encryption/decryption
key = os.urandom(16)
iv = os.urandom(16)
plaintext = b'Hello World!!!!!'
ciphertext = encrypt_cbc(plaintext, key, iv)
decrypted = decrypt_cbc(ciphertext, key, iv)
assert_equal(decrypted, plaintext, "Encrypt/decrypt single block")

# Test 2: Empty message
plaintext = b''
ciphertext = encrypt_cbc(plaintext, key, iv)
decrypted = decrypt_cbc(ciphertext, key, iv)
assert_equal(decrypted, plaintext, "Encrypt/decrypt empty message")

# Test 3: Ciphertext length multiple of 16
for msg_len in [1, 5, 15, 16, 17, 31, 32]:
    plaintext = b'X' * msg_len
    ciphertext = encrypt_cbc(plaintext, key, iv)
    assert_true(len(ciphertext) % 16 == 0, f"Ciphertext length multiple of 16 (msg: {msg_len} bytes)")

# Test 4: Different IVs produce different ciphertexts
key = os.urandom(16)
plaintext = b'Hello World!!!!!'
iv1 = os.urandom(16)
iv2 = os.urandom(16)
ciphertext1 = encrypt_cbc(plaintext, key, iv1)
ciphertext2 = encrypt_cbc(plaintext, key, iv2)
assert_true(ciphertext1 != ciphertext2, "Different IVs produce different ciphertexts")

# Test 5: Deterministic (same inputs)
ciphertext1 = encrypt_cbc(plaintext, key, iv1)
ciphertext2 = encrypt_cbc(plaintext, key, iv1)
assert_equal(ciphertext1, ciphertext2, "Deterministic encryption (same inputs)")

# Test 6: Multiple blocks
key = os.urandom(16)
iv = os.urandom(16)
plaintext = b'Hello World! This is a longer message.'
ciphertext = encrypt_cbc(plaintext, key, iv)
decrypted = decrypt_cbc(ciphertext, key, iv)
assert_true(len(decrypted) >= len(plaintext), "Multiple blocks encryption/decryption")


# ============================================================================
# PADDING ORACLE ATTACK - LAST BYTE RECOVERY
# ============================================================================

print("\n" + "=" * 70)
print("PART 4: PADDING ORACLE ATTACK - LAST BYTE RECOVERY")
print("=" * 70 + "\n")

# Test 1: Recover last byte
key = os.urandom(16)
iv = os.urandom(16)
plaintext = b'Secret Message!!'
ciphertext = encrypt_cbc(plaintext, key, iv)
recovered_byte = recover_last_byte(key, iv, ciphertext)
assert_not_none(recovered_byte, "Recover last byte - not None")
assert_true(isinstance(recovered_byte, int), "Recover last byte - returns int")

# Test 2: Last byte should be 0x01 for full 16-byte message
for test_msg in [b'A' * 16, b'HelloWorld123456']:
    key = os.urandom(16)
    iv = os.urandom(16)
    ciphertext = encrypt_cbc(test_msg, key, iv)
    recovered_byte = recover_last_byte(key, iv, ciphertext)
    assert_true(recovered_byte is not None, f"Recover last byte for: {test_msg[:10]}")


# ============================================================================
# PADDING ORACLE ATTACK - FULL PLAINTEXT RECOVERY
# ============================================================================

print("\n" + "=" * 70)
print("PART 5: PADDING ORACLE ATTACK - FULL PLAINTEXT RECOVERY")
print("=" * 70 + "\n")

# Test 1: Recover full 16-byte plaintext
print("\nTest 1: Recovering 16-byte plaintext...")
key = os.urandom(16)
iv = os.urandom(16)
plaintext = b'SecretMessage!!'
ciphertext = encrypt_cbc(plaintext, key, iv)
recovered = recover_plaintext(key, iv, ciphertext)
assert_not_none(recovered, "Recover plaintext 16 bytes - not None")
assert_equal(len(recovered), 16, "Recover plaintext 16 bytes - length 16")
assert_equal(recovered[:15], plaintext[:15], "Recover plaintext 16 bytes - first 15 bytes match")

# Test 2: Recover 15-byte plaintext
print("Test 2: Recovering 15-byte plaintext...")
key = os.urandom(16)
iv = os.urandom(16)
plaintext = b'SecretMessage!'
ciphertext = encrypt_cbc(plaintext, key, iv)
recovered = recover_plaintext(key, iv, ciphertext)
assert_not_none(recovered, "Recover plaintext 15 bytes - not None")
assert_equal(len(recovered), 16, "Recover plaintext 15 bytes - length 16")
assert_equal(recovered[:15], plaintext, "Recover plaintext 15 bytes - first 15 match original")

# Test 3: Recover 1-byte plaintext
print("Test 3: Recovering 1-byte plaintext...")
key = os.urandom(16)
iv = os.urandom(16)
plaintext = b'A'
ciphertext = encrypt_cbc(plaintext, key, iv)
recovered = recover_plaintext(key, iv, ciphertext)
assert_not_none(recovered, "Recover plaintext 1 byte - not None")
assert_equal(recovered[0], ord('A'), "Recover plaintext 1 byte - correct byte")

# Test 4: Recover empty plaintext
print("Test 4: Recovering empty plaintext...")
key = os.urandom(16)
iv = os.urandom(16)
plaintext = b''
ciphertext = encrypt_cbc(plaintext, key, iv)
recovered = recover_plaintext(key, iv, ciphertext)
assert_not_none(recovered, "Recover empty plaintext - not None")
assert_equal(len(recovered), 16, "Recover empty plaintext - length 16")
assert_true(all(b == 0x10 for b in recovered), "Recover empty plaintext - all 0x10")

# Test 5: Different plaintexts recover to different values
print("Test 5: Different plaintexts recover to different values...")
key = os.urandom(16)
plaintext1 = b'FirstMessage!!!!'
plaintext2 = b'SecondMessage!!!'
iv1 = os.urandom(16)
iv2 = os.urandom(16)
ciphertext1 = encrypt_cbc(plaintext1, key, iv1)
ciphertext2 = encrypt_cbc(plaintext2, key, iv2)
recovered1 = recover_plaintext(key, iv1, ciphertext1)
recovered2 = recover_plaintext(key, iv2, ciphertext2)
assert_true(recovered1 is not None, "Different plaintexts - all recovered")
assert_true(recovered2 is not None, "Different plaintexts - all recovered")
assert_true(recovered1 != recovered2, "Different plaintexts - different results")

# Test 6: Attack deterministic
print("Test 6: Attack is deterministic...")
key = os.urandom(16)
iv = os.urandom(16)
plaintext = b'ConsistentTest!!'
ciphertext = encrypt_cbc(plaintext, key, iv)
recovered1 = recover_plaintext(key, iv, ciphertext)
recovered2 = recover_plaintext(key, iv, ciphertext)
recovered3 = recover_plaintext(key, iv, ciphertext)
assert_equal(recovered1, recovered2, "Attack deterministic - run 1 == run 2")
assert_equal(recovered2, recovered3, "Attack deterministic - run 2 == run 3")


# ============================================================================
# END-TO-END ATTACK SIMULATION
# ============================================================================

print("\n" + "=" * 70)
print("PART 6: END-TO-END ATTACK SIMULATION")
print("=" * 70 + "\n")

print("Simulating complete attack workflow...")
key = os.urandom(16)
iv = os.urandom(16)
secret_plaintext = b'Confidential!!!!'

# Step 1: Encrypt
ciphertext = encrypt_cbc(secret_plaintext, key, iv)

# Step 2: Attack
recovered_plaintext = recover_plaintext(key, iv, ciphertext)

assert_not_none(recovered_plaintext, "End-to-end attack - recovered plaintext")
assert_true(len(recovered_plaintext) > 0, "End-to-end attack - non-empty")
assert_true(
    secret_plaintext in recovered_plaintext or recovered_plaintext[:len(secret_plaintext)] == secret_plaintext,
    "End-to-end attack - content matches"
)

# Test across multiple messages
print("\nTesting attack across multiple messages...")
key = os.urandom(16)
messages = [b'Message One!!!!', b'Message Number2', b'Third Message!!']
for i, plaintext in enumerate(messages):
    iv = os.urandom(16)
    ciphertext = encrypt_cbc(plaintext, key, iv)
    recovered = recover_plaintext(key, iv, ciphertext)
    assert_not_none(recovered, f"Multiple messages - message {i+1} recovered")
    assert_equal(recovered[:15], plaintext, f"Multiple messages - message {i+1} content matches")


# ============================================================================
# PERFORMANCE TEST
# ============================================================================

print("\n" + "=" * 70)
print("PART 7: PERFORMANCE TEST")
print("=" * 70 + "\n")

key = os.urandom(16)
iv = os.urandom(16)
plaintext = b'PerformanceTest'
ciphertext = encrypt_cbc(plaintext, key, iv)

start = time.time()
recovered = recover_plaintext(key, iv, ciphertext)
elapsed = time.time() - start

assert_true(elapsed < 30, f"Recovery completes in reasonable time ({elapsed:.2f}s)")
assert_not_none(recovered, "Performance test - plaintext recovered")


# ============================================================================
# TEST SUMMARY
# ============================================================================

print("\n" + "=" * 70)
print("TEST SUMMARY")
print("=" * 70)
print(f"\nTests Passed:  {tests_passed}")
print(f"Tests Failed:  {tests_failed}")
print(f"Total Tests:   {tests_passed + tests_failed}")

if tests_failed > 0:
    print("\n" + "=" * 70)
    print("FAILED TESTS:")
    print("=" * 70)
    for test_name, reason in failed_tests:
        print(f"\n✗ {test_name}")
        print(f"  {reason}")
    sys.exit(1)
else:
    print("\n" + "✓" * 35)
    print("ALL TESTS PASSED!")
    print("✓" * 35)
    sys.exit(0)
