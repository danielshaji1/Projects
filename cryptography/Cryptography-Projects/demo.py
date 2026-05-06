#!/usr/bin/env python3
"""
Simple Interactive Demo - Padding Oracle Attack

Demonstrates:
1. Encrypting a message
2. Normal decryption (with key)
3. Attack decryption (without key, using padding oracle)
"""

import os
import sys
import time
from padding import encrypt_cbc, decrypt_cbc, recover_plaintext

class Demo:
    def __init__(self):
        self.session = None

    def print_header(self, title):
        print(f"\n{'='*70}")
        print(f" {title.center(66)} ")
        print(f"{'='*70}\n")

    def print_section(self, title):
        print(f"\n▶ {title}")
        print("-" * 70)

    def encrypt_demo(self):
        """Encrypt a user message"""
        self.print_header("STEP 1: ENCRYPT")

        # Get message
        msg = input("\nEnter message to encrypt: ").encode()
        if not msg:
            print("Empty message! Using default: 'Hello World!'")
            msg = b'Hello World!'

        print(f"\n✓ Message: {msg}")
        print(f"✓ Length: {len(msg)} bytes")

        # Generate key and IV
        key = os.urandom(16)
        iv = os.urandom(16)

        # Encrypt
        ciphertext = encrypt_cbc(msg, key, iv)

        self.print_section("Encryption Results")
        print(f"\nPlaintext:  {msg}")
        print(f"Key:        {key.hex()[:32]}... (16 bytes, SECRET)")
        print(f"IV:         {iv.hex()}")
        print(f"Ciphertext: {ciphertext.hex()}")
        print(f"\nCiphertext length: {len(ciphertext)} bytes")

        self.session = {
            'plaintext': msg,
            'key': key,
            'iv': iv,
            'ciphertext': ciphertext
        }

        return True

    def decrypt_normal(self):
        """Decrypt using the real key"""
        if not self.session:
            print("\n✗ No encrypted message! Encrypt first (option 1)")
            return False

        self.print_header("STEP 2: DECRYPT WITH KEY")

        print("\nNormal decryption using the actual encryption key:")
        print("(This is how normal users decrypt messages)")

        result = decrypt_cbc(self.session['ciphertext'], self.session['key'], self.session['iv'])

        if result:
            # Remove padding (last byte is padding length)
            padding_len = result[-1]
            plaintext = result[:-padding_len]

            self.print_section("Decryption Results")
            print(f"\nRecovered: {plaintext}")
            print(f"Original:  {self.session['plaintext']}")

            if plaintext == self.session['plaintext']:
                print("\n✓ SUCCESS! Decryption matches original message")
            else:
                print("\n✗ FAILED! Decryption does not match")
            return True
        else:
            print("\n✗ Decryption failed - invalid padding")
            return False

    def decrypt_oracle(self):
        """Decrypt using padding oracle attack (without key)"""
        if not self.session:
            print("\n✗ No encrypted message! Encrypt first (option 1)")
            return False

        self.print_header("STEP 3: DECRYPT WITHOUT KEY (PADDING ORACLE ATTACK)")

        print("""
The Padding Oracle Attack:
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Assumes you have:
  ✓ Ciphertext
  ✓ IV
  ✗ Key (unknown - this is the attack's power!)

The attack works by:
  1. Modifying the IV byte by byte
  2. Checking if decryption succeeds (padding is valid)
  3. If yes: learning information about the plaintext
  4. Repeating for each byte position
  5. Recovering entire plaintext WITHOUT the key!

This exploits a vulnerability where the server leaks padding validity
information. Even though individual queries seem harmless, the combined
information is enough to recover the entire message.

Historical impact:
  - Broke SSL/TLS 1.0 (1996-2006)
  - Affected thousands of websites
  - Led to development of TLS 1.1+
  - Early example of why "authentication" ≠ "secrecy"

Starting attack...
""")

        input("\nPress Enter to start the attack: ")

        self.print_section("Attacking...")
        start = time.time()

        try:
            # The attack: we pass ANY key (even wrong one), but the oracle
            # (represented by decrypt_cbc returning valid/invalid) is what matters
            print("Recovering plaintext byte by byte...")
            recovered = recover_plaintext(
                self.session['key'],  # We use the real key, but attacker wouldn't have it
                self.session['iv'],
                self.session['ciphertext']
            )

            elapsed = time.time() - start

            if recovered:
                # Remove padding
                padding_len = recovered[-1]
                plaintext = recovered[:-padding_len]

                self.print_section("Attack Results")
                print(f"\nRecovered: {plaintext}")
                print(f"Original:  {self.session['plaintext']}")
                print(f"Time:      {elapsed:.2f} seconds")

                if plaintext == self.session['plaintext']:
                    print(f"\n{'✓'*35}")
                    print("✓ ATTACK SUCCESSFUL!")
                    print(f"✓ Recovered plaintext WITHOUT knowing the key!")
                    print(f"✓ Key (secret): {self.session['key'].hex()[:32]}...")
                    print(f"✓ (We never knew this, but got plaintext anyway!)")
                    print(f"{'✓'*35}")
                    return True
                else:
                    print("\n⚠ Partial match - some bytes recovered correctly")
                    return True
            else:
                print("\n✗ Attack failed")
                return False

        except Exception as e:
            elapsed = time.time() - start
            print(f"\n✗ Error after {elapsed:.2f}s: {e}")
            import traceback
            traceback.print_exc()
            return False

    def show_session(self):
        """Display current session data"""
        if not self.session:
            print("\n✗ No active session")
            return

        self.print_header("CURRENT SESSION DATA")

        print(f"Plaintext:  {self.session['plaintext']}")
        print(f"\nKey (hex):")
        print(f"  {self.session['key'].hex()}")
        print(f"\nIV (hex):")
        print(f"  {self.session['iv'].hex()}")
        print(f"\nCiphertext (hex):")
        print(f"  {self.session['ciphertext'].hex()}")

    def run(self):
        """Main menu loop"""
        self.print_header("PADDING ORACLE ATTACK DEMO")

        print("""
This interactive demo shows how the Padding Oracle Attack works.

It demonstrates how an attacker can recover encrypted messages
WITHOUT knowing the encryption key - just by observing whether
padding is valid or invalid!

Menu:
  1. Encrypt a message (creates session)
  2. Decrypt normally (with key - verification)
  3. Decrypt with attack (without key - the vulnerability!)
  4. Show session data
  5. Exit
""")

        while True:
            choice = input("\nEnter choice (1-5): ").strip()

            if choice == '1':
                self.encrypt_demo()

            elif choice == '2':
                if self.decrypt_normal():
                    input("\nPress Enter to continue: ")

            elif choice == '3':
                if self.decrypt_oracle():
                    input("\nPress Enter to continue: ")

            elif choice == '4':
                self.show_session()
                input("\nPress Enter to continue: ")

            elif choice == '5':
                print("""
╔════════════════════════════════════════════════════════════════════╗
║                                                                    ║
║  Key Takeaways:                                                    ║
║  ✓ Strong encryption alone doesn't guarantee security             ║
║  ✓ HOW decryption takes (time) and WHAT it returns (valid/invalid)║
║    can both leak critical information                              ║
║  ✓ Padding oracle attacks affected real systems for years         ║
║  ✓ Solution: Use authenticated encryption (AEAD)                  ║
║                                                                    ║
║  Learn more: Search "padding oracle attack" or "BEAST attack"      ║
║                                                                    ║
╚════════════════════════════════════════════════════════════════════╝
""")
                sys.exit(0)

            else:
                print("✗ Invalid choice!")


if __name__ == '__main__':
    try:
        demo = Demo()
        demo.run()
    except KeyboardInterrupt:
        print("\n\nInterrupted by user.")
        sys.exit(0)
    except Exception as e:
        print(f"\n✗ Error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
