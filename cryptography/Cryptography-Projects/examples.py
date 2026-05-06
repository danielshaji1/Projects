#!/usr/bin/env python3
"""
Simple Examples - How to use the Padding Oracle Attack

This script shows practical examples of:
1. Encrypting a message
2. Normal decryption
3. Attacking with padding oracle
"""

import os
import sys

# Add project to path
sys.path.insert(0, '/Users/danielshaji/Downloads/Cryptography Projects')

from padding import encrypt_cbc, decrypt_cbc, recover_plaintext

def example1_basic_encryption():
    """Example 1: Basic encryption and decryption"""
    print("\n" + "="*70)
    print("EXAMPLE 1: Basic Encryption & Decryption")
    print("="*70)

    # Create message and keys
    plaintext = b"Hello World!"
    key = os.urandom(16)
    iv = os.urandom(16)

    print(f"\nOriginal message: {plaintext}")
    print(f"Key (hex):        {key.hex()}")
    print(f"IV (hex):         {iv.hex()}")

    # Encrypt
    ciphertext = encrypt_cbc(plaintext, key, iv)
    print(f"\nCiphertext:       {ciphertext.hex()}")

    # Decrypt normally
    recovered = decrypt_cbc(ciphertext, key, iv)
    padding_len = recovered[-1]
    message = recovered[:-padding_len]

    print(f"Recovered:        {message}")
    print(f"Match:            {'✓ YES' if message == plaintext else '✗ NO'}")


def example2_oracle_attack():
    """Example 2: Padding Oracle Attack"""
    print("\n" + "="*70)
    print("EXAMPLE 2: Padding Oracle Attack (Without Key)")
    print("="*70)

    # Setup
    secret_plaintext = b"Attack Works!!!!"
    secret_key = os.urandom(16)  # Attacker doesn't know this
    iv = os.urandom(16)

    print(f"\nSecret message: {secret_plaintext}")
    print(f"Secret key:     {secret_key.hex()} ← Attacker doesn't know this!")
    print(f"IV:             {iv.hex()}")

    # Encrypt (victim does this)
    ciphertext = encrypt_cbc(secret_plaintext, secret_key, iv)
    print(f"\nCiphertext:     {ciphertext.hex()}")

    # Attack (attacker does this)
    print("\n" + "-"*70)
    print("Attacking (recovering plaintext without key)...")
    print("-"*70)

    import time
    start = time.time()

    # Attacker uses the padding oracle to recover plaintext
    # Note: We pass the real key here because decrypt_cbc needs it to act as oracle
    # But in real attack, attacker would send ciphertext to server
    recovered_with_padding = recover_plaintext(secret_key, iv, ciphertext)
    padding_len = recovered_with_padding[-1]
    recovered_plaintext = recovered_with_padding[:-padding_len]

    elapsed = time.time() - start

    print(f"\nRecovered:      {recovered_plaintext}")
    print(f"Time taken:     {elapsed:.2f} seconds")
    print(f"Match:          {'✓ YES' if recovered_plaintext == secret_plaintext else '✗ NO'}")

    print(f"\n{'-'*70}")
    print("✓ Attack successful! Recovered plaintext WITHOUT the secret key!")
    print("✓ The attacker never knew the key, but got the message anyway!")


def example3_demonstrate_oracle():
    """Example 3: Demonstrate the Oracle"""
    print("\n" + "="*70)
    print("EXAMPLE 3: Understanding the Padding Oracle")
    print("="*70)

    key = os.urandom(16)
    iv = os.urandom(16)
    plaintext = b"Test Message!!!!"

    ciphertext = encrypt_cbc(plaintext, key, iv)

    print(f"\nOriginal plaintext: {plaintext}")
    print(f"Ciphertext:         {ciphertext.hex()}")

    print("\n" + "-"*70)
    print("Testing the Oracle behavior:")
    print("-"*70)

    # Test 1: Unmodified (should succeed)
    result = decrypt_cbc(ciphertext, key, iv)
    print(f"\nTest 1: Unmodified ciphertext")
    print(f"  Oracle says: {'✓ VALID' if result else '✗ INVALID'}")

    # Test 2: Modify last byte of ciphertext
    modified = ciphertext[:-1] + bytes([ciphertext[-1] ^ 0xFF])
    result = decrypt_cbc(modified, key, iv)
    print(f"\nTest 2: Modified last byte of ciphertext")
    print(f"  Oracle says: {'✓ VALID' if result else '✗ INVALID'}")

    # Test 3: Modify IV's last byte (this is what the attack does!)
    modified_iv = iv[:-1] + bytes([iv[-1] ^ 0xFF])
    result = decrypt_cbc(ciphertext, key, modified_iv)
    print(f"\nTest 3: Modified IV's last byte")
    print(f"  Oracle says: {'✓ VALID' if result else '✗ INVALID'}")

    print(f"\n{'-'*70}")
    print("By trying all 256 possible IV values, the attacker can figure")
    print("out which ones produce valid padding. This tells them about")
    print("the plaintext, byte by byte!")


def example4_custom_message():
    """Example 4: User can specify custom message"""
    print("\n" + "="*70)
    print("EXAMPLE 4: Try Your Own Message")
    print("="*70)

    message = input("\nEnter a message (or press Enter for default): ").strip()
    if not message:
        message = "Try any message!"

    plaintext = message.encode()[:16]  # Limit to 16 bytes for simplicity
    if len(plaintext) == 0:
        plaintext = b"Default message"

    key = os.urandom(16)
    iv = os.urandom(16)

    print(f"\n✓ Plaintext:  {plaintext}")
    print(f"✓ Length:     {len(plaintext)} bytes")

    # Encrypt
    ciphertext = encrypt_cbc(plaintext, key, iv)
    print(f"✓ Encrypted:  {ciphertext.hex()[:32]}...")

    # Decrypt normally
    recovered1 = decrypt_cbc(ciphertext, key, iv)
    if recovered1:
        padding_len = recovered1[-1]
        recovered1_clean = recovered1[:-padding_len]
        print(f"✓ Decrypted:  {recovered1_clean}")

    # Recover with oracle
    print("\nRecovering with oracle attack...")
    import time
    start = time.time()
    recovered2 = recover_plaintext(key, iv, ciphertext)
    elapsed = time.time() - start

    if recovered2:
        padding_len = recovered2[-1]
        recovered2_clean = recovered2[:-padding_len]
        print(f"✓ Recovered:  {recovered2_clean}")
        print(f"✓ Time:       {elapsed:.2f} seconds")

        if recovered2_clean == plaintext:
            print(f"\n✓✓✓ Attack succeeded! Both methods match! ✓✓✓")


def main():
    """Run all examples"""
    print("\n")
    print("╔" + "="*68 + "╗")
    print("║" + "  PADDING ORACLE ATTACK - PRACTICAL EXAMPLES  ".center(68) + "║")
    print("╚" + "="*68 + "╝")

    print("""
This script demonstrates:
1. Basic encryption and decryption
2. Padding Oracle Attack (recovering plaintext without key)
3. Understanding the oracle behavior
4. Try your own message
""")

    try:
        example1_basic_encryption()
        input("\nPress Enter to continue to Example 2...")

        example2_oracle_attack()
        input("\nPress Enter to continue to Example 3...")

        example3_demonstrate_oracle()
        input("\nPress Enter to continue to Example 4...")

        example4_custom_message()

        print("\n" + "="*70)
        print("CONCLUSION")
        print("="*70)
        print("""
You've seen:
✓ How AES-CBC encryption works
✓ How padding oracle attack works
✓ That plaintext can be recovered WITHOUT the key
✓ How the oracle's yes/no answer is enough to break encryption

Key takeaway:
  Secure encryption ≠ secure protocol
  Implementation details matter!

For more information:
  ✓ Run: python3 demo.py (interactive menu)
  ✓ Run: python3 run_tests.py (comprehensive tests)
  ✓ Read: README.md (detailed documentation)
""")

    except KeyboardInterrupt:
        print("\n\nInterrupted.")
        sys.exit(0)
    except Exception as e:
        print(f"\n✗ Error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)


if __name__ == '__main__':
    main()
