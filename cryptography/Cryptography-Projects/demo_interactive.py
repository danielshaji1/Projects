#!/usr/bin/env python3
"""
Interactive Padding Oracle Attack Demo

Allows users to:
1. Encrypt a message with a secret key
2. Attempt to decrypt using:
   a) The padding oracle attack (without knowing the key)
   b) Normal decryption (with the key, for verification)
"""

import os
import sys
import time
from padding import encrypt_cbc, decrypt_cbc, recover_plaintext, recover_last_byte

# Color codes for terminal output
class Colors:
    HEADER = '\033[95m'
    BLUE = '\033[94m'
    CYAN = '\033[96m'
    GREEN = '\033[92m'
    YELLOW = '\033[93m'
    RED = '\033[91m'
    END = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


def print_header(text):
    print(f"\n{Colors.BOLD}{Colors.HEADER}{'=' * 70}")
    print(f"{text.center(70)}")
    print(f"{'=' * 70}{Colors.END}\n")


def print_section(text):
    print(f"{Colors.BOLD}{Colors.CYAN}► {text}{Colors.END}")


def print_success(text):
    print(f"{Colors.GREEN}✓ {text}{Colors.END}")


def print_warning(text):
    print(f"{Colors.YELLOW}⚠ {text}{Colors.END}")


def print_error(text):
    print(f"{Colors.RED}✗ {text}{Colors.END}")


def print_info(text):
    print(f"{Colors.BLUE}ℹ {text}{Colors.END}")


def bytes_to_hex(data):
    """Convert bytes to hex string"""
    return data.hex()


def hex_to_bytes(hex_str):
    """Convert hex string to bytes"""
    try:
        return bytes.fromhex(hex_str.replace(" ", ""))
    except ValueError:
        return None


def format_bytes(data, max_len=64):
    """Format bytes nicely for display"""
    if len(data) <= max_len:
        return f"{data} (hex: {bytes_to_hex(data)})"
    else:
        return f"{data[:max_len//2]}...{data[-max_len//2:]} (hex: {bytes_to_hex(data)})"


def encrypt_message():
    """Encrypt a user-provided message"""
    print_header("ENCRYPT A MESSAGE")

    # Get plaintext from user
    print_section("Step 1: Enter the plaintext message")
    plaintext = input(f"{Colors.YELLOW}Enter message to encrypt: {Colors.END}").encode()

    if not plaintext:
        print_error("Empty message!")
        return None

    print_info(f"Message: {plaintext}")
    print_info(f"Length: {len(plaintext)} bytes")

    # Generate random key and IV
    print_section("Step 2: Generating encryption key and IV")
    key = os.urandom(16)
    iv = os.urandom(16)

    print_success("Key generated (16 bytes)")
    print_success("IV generated (16 bytes)")

    # Encrypt
    print_section("Step 3: Encrypting with AES-CBC")
    ciphertext = encrypt_cbc(plaintext, key, iv)
    print_success(f"Message encrypted!")
    print_info(f"Ciphertext length: {len(ciphertext)} bytes")

    # Display results
    print_header("ENCRYPTION RESULTS")
    print(f"{Colors.BOLD}Plaintext:{Colors.END}")
    print(f"  {plaintext}")
    print()
    print(f"{Colors.BOLD}Key (SECRET - DO NOT SHARE):{Colors.END}")
    print(f"  {bytes_to_hex(key)}")
    print()
    print(f"{Colors.BOLD}IV (needed for decryption):{Colors.END}")
    print(f"  {bytes_to_hex(iv)}")
    print()
    print(f"{Colors.BOLD}Ciphertext (encrypted message):{Colors.END}")
    print(f"  {bytes_to_hex(ciphertext)}")
    print()

    return {
        'plaintext': plaintext,
        'key': key,
        'iv': iv,
        'ciphertext': ciphertext
    }


def decrypt_with_key(ciphertext, iv, key):
    """Decrypt using the actual key (for verification)"""
    print_section("Decrypting with key...")
    result = decrypt_cbc(ciphertext, key, iv)

    if result:
        # Remove padding
        padding_length = result[-1]
        plaintext = result[:-padding_length]
        return plaintext
    return None


def decrypt_with_oracle(ciphertext, iv):
    """Decrypt using the padding oracle attack"""
    print_section("Starting padding oracle attack...")
    print_info("Note: This recovers the plaintext WITHOUT knowing the key")

    # Start timing
    start_time = time.time()
    query_count = 0

    # Modified decrypt_cbc that counts queries
    def decrypt_cbc_with_count(ciphertext, key_unused, iv):
        nonlocal query_count
        query_count += 1
        # We don't have the key, so we can't actually decrypt
        # But decrypt_cbc acts as the oracle
        return None

    print_warning("Attempting to recover plaintext...")
    print_info("This may take 5-30 seconds depending on message length...")
    print()

    try:
        # Use the recover_plaintext function which uses the oracle
        recovered = recover_plaintext(None, iv, ciphertext)  # Pass None for key since we use the oracle

        elapsed = time.time() - start_time

        if recovered:
            # Remove padding
            padding_length = recovered[-1]
            plaintext = recovered[:-padding_length]

            print_success(f"Attack succeeded in {elapsed:.2f} seconds!")
            return plaintext
        else:
            print_error("Attack failed - no plaintext recovered")
            return None

    except Exception as e:
        elapsed = time.time() - start_time
        print_error(f"Attack failed after {elapsed:.2f} seconds")
        print_error(f"Error: {e}")
        return None


def main_menu():
    """Main menu loop"""
    print_header("PADDING ORACLE ATTACK - INTERACTIVE DEMO")

    print(f"""
{Colors.BOLD}This demo shows how a padding oracle attack works:{Colors.END}

1. {Colors.CYAN}Encrypt a message{Colors.END} - You provide a plaintext message
   - System generates a random key (kept secret)
   - Message encrypted with AES-CBC
   - You get ciphertext and IV

2. {Colors.CYAN}Decrypt with key{Colors.END} - Decrypt knowing the secret key
   - Normal AES-CBC decryption
   - Works perfectly (verification)

3. {Colors.CYAN}Decrypt without key{Colors.END} - Padding oracle attack
   - Recovers plaintext WITHOUT knowing the secret key
   - Uses padding oracle vulnerability
   - Demonstrates the security flaw

{Colors.YELLOW}Let's start!{Colors.END}
""")

    session_data = None

    while True:
        print_header("MAIN MENU")
        print("""
1. Encrypt a new message
2. Decrypt with key (if message encrypted)
3. Decrypt without key - Padding Oracle Attack (if message encrypted)
4. Show current session data (if message encrypted)
5. Exit
        """)

        choice = input(f"{Colors.YELLOW}Enter choice (1-5): {Colors.END}").strip()

        if choice == '1':
            session_data = encrypt_message()

        elif choice == '2':
            if not session_data:
                print_error("No encrypted message! Encrypt a message first (option 1)")
                input("Press Enter to continue...")
                continue

            print_header("DECRYPT WITH KEY")
            print_info("Using the actual encryption key for normal decryption")

            plaintext = decrypt_with_key(session_data['ciphertext'], session_data['iv'], session_data['key'])

            if plaintext:
                print_success("Decryption successful!")
                print()
                print(f"{Colors.BOLD}Original plaintext:{Colors.END}")
                print(f"  {Colors.GREEN}{plaintext}{Colors.END}")
                print()
                print(f"{Colors.BOLD}Matches encrypted message?{Colors.END}")
                if plaintext == session_data['plaintext']:
                    print_success("YES - Encryption/Decryption verified!")
                else:
                    print_error("NO - Something went wrong")
            else:
                print_error("Decryption failed - invalid padding")

            input("\nPress Enter to continue...")

        elif choice == '3':
            if not session_data:
                print_error("No encrypted message! Encrypt a message first (option 1)")
                input("Press Enter to continue...")
                continue

            print_header("PADDING ORACLE ATTACK - RECOVER WITHOUT KEY")
            print(f"""
{Colors.BOLD}What is a Padding Oracle Attack?{Colors.END}

A padding oracle attack exploits a security flaw where the server
reveals whether padding validation succeeded. Even though the server
doesn't return the plaintext, this "yes/no" information is enough to
recover the entire message!

{Colors.BOLD}How it works:{Colors.END}
1. Attacker has: Ciphertext + IV (but NOT the key)
2. Attacker modifies the IV and sends the ciphertext
3. Server tries to decrypt and checks padding
4. Server says "valid" or "invalid padding"
5. Attacker learns information about plaintext
6. Repeat for each byte - eventually recover full message

{Colors.BOLD}In this demo:{Colors.END}
- The padding oracle is the decrypt_cbc() function
- It returns None if padding is invalid (oracle says "invalid")
- It returns plaintext if padding is valid (oracle says "valid")
- We exploit this to recover plaintext WITHOUT the key!
""")

            print_warning("Starting attack... This may take a few seconds")
            print()

            plaintext = decrypt_with_oracle(session_data['ciphertext'], session_data['iv'])

            if plaintext:
                print()
                print_success("ATTACK SUCCESSFUL!")
                print()
                print(f"{Colors.BOLD}Recovered plaintext:{Colors.END}")
                print(f"  {Colors.GREEN}{plaintext}{Colors.END}")
                print()
                print(f"{Colors.BOLD}Matches original?{Colors.END}")
                if plaintext == session_data['plaintext']:
                    print_success("YES! - The attack recovered the correct plaintext!")
                    print()
                    print(f"{Colors.BOLD}Key insight:{Colors.END}")
                    print(f"  Original key: {bytes_to_hex(session_data['key'])}")
                    print(f"  {Colors.YELLOW}↑ We never knew this, but recovered plaintext anyway!{Colors.END}")
                else:
                    print_warning("PARTIAL - Some bytes may not match")
            else:
                print_error("Attack failed - could not recover plaintext")

            input("\nPress Enter to continue...")

        elif choice == '4':
            if not session_data:
                print_error("No session data")
                input("Press Enter to continue...")
                continue

            print_header("CURRENT SESSION DATA")
            print(f"{Colors.BOLD}Plaintext:{Colors.END}")
            print(f"  {session_data['plaintext']}")
            print()
            print(f"{Colors.BOLD}Key (hex, 16 bytes):{Colors.END}")
            print(f"  {bytes_to_hex(session_data['key'])}")
            print()
            print(f"{Colors.BOLD}IV (hex, 16 bytes):{Colors.END}")
            print(f"  {bytes_to_hex(session_data['iv'])}")
            print()
            print(f"{Colors.BOLD}Ciphertext (hex, {len(session_data['ciphertext'])} bytes):{Colors.END}")
            print(f"  {bytes_to_hex(session_data['ciphertext'])}")

            input("\nPress Enter to continue...")

        elif choice == '5':
            print_header("GOODBYE")
            print("""
Thank you for exploring the Padding Oracle Attack!

Key takeaways:
✓ Even secure encryption (AES-CBC) can be broken if it leaks padding info
✓ The padding oracle attack recovers plaintext WITHOUT the key
✓ This vulnerability affected SSL/TLS 1.0 and earlier
✓ Solution: Use authenticated encryption (AEAD) like AES-GCM

Learn more: https://en.wikipedia.org/wiki/Padding_oracle_attack
""")
            sys.exit(0)
        else:
            print_error("Invalid choice! Enter 1-5")
            input("Press Enter to continue...")


if __name__ == '__main__':
    try:
        main_menu()
    except KeyboardInterrupt:
        print(f"\n\n{Colors.YELLOW}Program interrupted by user{Colors.END}")
        sys.exit(0)
    except Exception as e:
        print_error(f"Unexpected error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
