#!/usr/bin/env python3
"""
Simple diagnostic test for Padding Oracle Attack
"""
import sys
import os

# Change to project directory
project_dir = '/Users/danielshaji/Downloads/Cryptography Projects'
os.chdir(project_dir)
sys.path.insert(0, project_dir)

print("=" * 70)
print("PADDING ORACLE ATTACK - DIAGNOSTIC TEST")
print("=" * 70)

# Test imports
print("\n[1/5] Testing imports...")
try:
    from padding import pad, check_pad, encrypt_cbc, decrypt_cbc, recover_last_byte, recover_plaintext, AES
    print("✓ All modules imported successfully")
except Exception as e:
    print(f"✗ Import failed: {e}")
    sys.exit(1)

# Test padding
print("\n[2/5] Testing PADDING function...")
try:
    msg = b'Test'
    padded = pad(msg)
    assert len(padded) == 16, f"Expected 16 bytes, got {len(padded)}"
    assert all(b == 12 for b in padded[4:]), "Padding bytes should be 12"
    print("✓ Padding function works correctly")
except Exception as e:
    print(f"✗ Padding test failed: {e}")
    sys.exit(1)

# Test AES-CBC encryption/decryption
print("\n[3/5] Testing AES-CBC ENCRYPTION/DECRYPTION...")
try:
    key = os.urandom(16)
    iv = os.urandom(16)
    plaintext = b'HelloWorld12345'  # 15 bytes - will be padded to 16

    print("  - Encrypting...")
    ciphertext = encrypt_cbc(plaintext, key, iv)
    assert len(ciphertext) == 16, f"Expected 16 bytes ciphertext, got {len(ciphertext)}"

    print("  - Decrypting...")
    decrypted = decrypt_cbc(ciphertext, key, iv)
    assert decrypted == plaintext, f"Decryption mismatch: {decrypted} != {plaintext}"

    print("✓ AES-CBC encryption/decryption works correctly")
except Exception as e:
    print(f"✗ AES-CBC test failed: {e}")
    sys.exit(1)

# Test last byte recovery
print("\n[4/5] Testing LAST BYTE RECOVERY...")
try:
    key = os.urandom(16)
    iv = os.urandom(16)
    plaintext = b'A' * 15  # 15 bytes - padded with 0x01

    print("  - Encrypting...")
    ciphertext = encrypt_cbc(plaintext, key, iv)

    print("  - Recovering last byte...")
    last_byte = recover_last_byte(key, iv, ciphertext)
    assert last_byte is not None, "Recovered byte should not be None"
    assert last_byte == 0x01, f"Last byte should be 0x01 (padding), got {hex(last_byte)}"

    print(f"✓ Last byte recovery works (recovered: {hex(last_byte)})")
except Exception as e:
    print(f"✗ Last byte recovery failed: {e}")
    import traceback
    traceback.print_exc()
    sys.exit(1)

# Test full plaintext recovery
print("\n[5/5] Testing FULL PLAINTEXT RECOVERY...")
try:
    key = os.urandom(16)
    iv = os.urandom(16)
    plaintext = b'SecretMessage!!'  # 15 bytes

    print(f"  - Original plaintext: {plaintext}")
    print("  - Encrypting...")
    ciphertext = encrypt_cbc(plaintext, key, iv)

    print("  - Recovering full plaintext (this may take a moment)...")
    recovered = recover_plaintext(key, iv, ciphertext)

    assert recovered is not None, "Recovered plaintext should not be None"
    assert len(recovered) == 15, f"Expected 15 bytes, got {len(recovered)}"
    assert recovered == plaintext, f"Plaintext mismatch: {recovered} != {plaintext}"

    print(f"✓ Full plaintext recovery works!")
    print(f"  - Recovered: {recovered}")

except Exception as e:
    print(f"✗ Full plaintext recovery failed: {e}")
    import traceback
    traceback.print_exc()
    sys.exit(1)

# Summary
print("\n" + "=" * 70)
print("✓ ALL DIAGNOSTIC TESTS PASSED!")
print("=" * 70)
print("\nThe Padding Oracle Attack implementation is working correctly!")
print("\nSummary:")
print("  ✓ Padding function validates correctly")
print("  ✓ AES-CBC encryption/decryption works")
print("  ✓ Last byte recovery attack works")
print("  ✓ Full plaintext recovery attack works")
print("\nThe padding oracle attack can successfully recover plaintext")
print("from ciphertext WITHOUT knowing the encryption key!")
