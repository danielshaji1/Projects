#!/usr/bin/env python3
"""
Interactive Test Suite for Padding Oracle Attack

Allows users to:
1. Input a message to encrypt/test
2. Encrypt it with a random key
3. Run various tests on the encrypted message
4. Demonstrate the padding oracle attack
5. Verify the attack recovers the correct plaintext
"""

import os
import sys
import time
from padding import encrypt_cbc, decrypt_cbc, recover_plaintext, recover_last_byte, pad, check_pad, AES

# Color codes
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
    print(f"\n{Colors.BOLD}{Colors.HEADER}{'=' * 80}")
    print(f"{text.center(80)}")
    print(f"{'=' * 80}{Colors.END}\n")


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


def format_hex(data, line_width=64):
    """Format bytes as hex with line breaks"""
    hex_str = data.hex()
    lines = [hex_str[i:i+line_width] for i in range(0, len(hex_str), line_width)]
    return '\n  '.join(lines)


def test_padding_function(message):
    """Test the padding function"""
    print_section("TEST 1: Padding Function")

    padded = pad(message)
    padding_length = len(padded) - len(message)

    print_info(f"Original message length: {len(message)} bytes")
    print_info(f"Padded message length: {len(padded)} bytes")
    print_info(f"Padding added: {padding_length} bytes")

    # Verify padding
    assert len(padded) % 16 == 0, "Padded length should be multiple of 16"
    print_success(f"✓ Padded to multiple of 16")

    if len(message) == 0:
        assert all(b == 16 for b in padded), "Empty message should pad with 16 bytes of 0x10"
        print_success(f"✓ Empty message padded correctly (all 0x10)")
    else:
        assert all(b == padding_length for b in padded[-padding_length:]), "Padding bytes incorrect"
        print_success(f"✓ Padding bytes all equal {padding_length}")

    return padded


def test_padding_validation(padded_message):
    """Test the padding validation"""
    print_section("TEST 2: Padding Validation")

    is_valid = check_pad(padded_message)
    assert is_valid, "Valid padding should pass validation"
    print_success("✓ Padded message passes validation")

    # Test with corrupted padding
    corrupted = padded_message[:-1] + bytes([padded_message[-1] ^ 0xFF])
    is_valid = check_pad(corrupted)
    assert not is_valid, "Corrupted padding should fail validation"
    print_success("✓ Corrupted padding correctly rejected")


def test_encryption_decryption(plaintext, key, iv):
    """Test AES-CBC encryption and decryption"""
    print_section("TEST 3: AES-CBC Encryption/Decryption")

    print_info(f"Plaintext: {plaintext}")
    print_info(f"Key (hex): {key.hex()}")
    print_info(f"IV (hex): {iv.hex()}")

    # Encrypt
    ciphertext = encrypt_cbc(plaintext, key, iv)
    print_success(f"✓ Message encrypted ({len(ciphertext)} bytes)")

    # Decrypt with key
    decrypted = decrypt_cbc(ciphertext, key, iv)
    assert decrypted is not None, "Decryption failed"
    print_success(f"✓ Message decrypted with key")

    # Remove padding to compare
    padding_length = decrypted[-1]
    decrypted_original = decrypted[:-padding_length]

    assert decrypted_original == plaintext, "Decrypted message doesn't match original"
    print_success(f"✓ Decrypted message matches original")

    return ciphertext


def test_last_byte_recovery(plaintext, key, iv, ciphertext):
    """Test recovery of the last byte"""
    print_section("TEST 4: Last Byte Recovery (Partial Attack)")

    print_info("Attempting to recover just the last byte using oracle...")

    start = time.time()
    last_byte = recover_last_byte(key, iv, ciphertext)
    elapsed = time.time() - start

    assert last_byte is not None, "Last byte recovery failed"
    print_success(f"✓ Last byte recovered in {elapsed:.2f}s")
    print_info(f"Recovered byte value: {hex(last_byte)}")
    print_info(f"For a full 16-byte message, this should be 0x01 (padding)")
    return last_byte


def test_full_oracle_attack(plaintext, key, iv, ciphertext):
    """Test the full padding oracle attack"""
    print_section("TEST 5: Full Padding Oracle Attack")

    print_info("Recovering the entire plaintext WITHOUT the encryption key...")
    print_warning("This demonstrates the core vulnerability!")
    print()

    start = time.time()
    recovered_padded = recover_plaintext(key, iv, ciphertext)
    elapsed = time.time() - start

    assert recovered_padded is not None, "Plaintext recovery failed"
    print_success(f"✓ Plaintext recovered in {elapsed:.2f}s")

    # Remove padding
    padding_length = recovered_padded[-1]
    recovered = recovered_padded[:-padding_length]

    print_info(f"Recovered message: {recovered}")
    print_info(f"Padding bytes removed: {padding_length}")

    return recovered


def verify_attack_success(original, recovered):
    """Verify the attack was successful"""
    print_section("TEST 6: Verify Attack Success")

    if original == recovered:
        print_success("✓ ATTACK SUCCESSFUL!")
        print_success("✓ Recovered plaintext matches original")
        return True
    else:
        print_error("✗ Attack failed - plaintext doesn't match")
        print_error(f"Original:  {original}")
        print_error(f"Recovered: {recovered}")
        return False


def run_test_suite_interactive():
    """Run the full interactive test suite"""
    print_header("PADDING ORACLE ATTACK - INTERACTIVE TEST SUITE")

    print(f"""
{Colors.BOLD}This test suite demonstrates and verifies the padding oracle attack.{Colors.END}

The attack works in these steps:
  1. User provides a plaintext message
  2. System encrypts it with AES-CBC using a random 128-bit key
  3. User sees the encrypted ciphertext (but not the key)
  4. Tests verify component functionality
  5. Attack attempts to recover plaintext WITHOUT the key
  6. Success is verified by comparing to original

{Colors.YELLOW}Let's start!{Colors.END}
""")

    while True:
        print_header("INTERACTIVE TEST SUITE - MAIN MENU")

        print("""
1. Enter a message and run full test suite
2. Use example messages (quick test)
3. Exit
        """)

        choice = input(f"{Colors.YELLOW}Enter your choice (1-3): {Colors.END}").strip()

        if choice == '1':
            run_custom_message_tests()

        elif choice == '2':
            run_example_tests()

        elif choice == '3':
            print_header("GOODBYE")
            print("Thank you for testing the Padding Oracle Attack!")
            sys.exit(0)

        else:
            print_error("Invalid choice! Please enter 1, 2, or 3")
            input("Press Enter to continue...")


def run_custom_message_tests():
    """Run tests with a user-provided message"""
    print_header("CUSTOM MESSAGE TEST")

    # Get message from user
    plaintext = input(f"{Colors.YELLOW}Enter a message to encrypt: {Colors.END}").encode()

    if not plaintext:
        print_error("Empty message! Using default test message.")
        plaintext = b"Hello, World!"

    print_info(f"Message: {plaintext}")
    print_info(f"Length: {len(plaintext)} bytes")

    # Generate key and IV
    key = os.urandom(16)
    iv = os.urandom(16)

    print_success(f"Key generated: {key.hex()[:32]}...")
    print_success(f"IV generated: {iv.hex()[:32]}...")

    # Run tests
    run_all_tests(plaintext, key, iv)


def run_example_tests():
    """Run tests with example messages"""
    print_header("EXAMPLE TESTS")

    examples = [
        (b"Hello World!", "Short message"),
        (b"Secret123!@#", "Message with special chars"),
        (b"A" * 16, "Full 16-byte block"),
        (b"1234567890abcdef", "Exactly 16 bytes"),
    ]

    for i, (plaintext, description) in enumerate(examples, 1):
        print_header(f"EXAMPLE {i}: {description}")

        key = os.urandom(16)
        iv = os.urandom(16)

        print_info(f"Message: {plaintext}")
        print_info(f"Length: {len(plaintext)} bytes")

        success = run_all_tests(plaintext, key, iv)

        if not success:
            print_error("Test failed!")
            break

        print()
        cont = input(f"{Colors.YELLOW}Continue to next example? (y/n): {Colors.END}").strip().lower()
        if cont != 'y':
            break

    print_header("EXAMPLE TESTS COMPLETE")


def run_all_tests(plaintext, key, iv):
    """Run the complete test suite on a message"""
    try:
        # Test 1: Padding function
        padded = test_padding_function(plaintext)
        print()

        # Test 2: Padding validation
        test_padding_validation(padded)
        print()

        # Test 3: Encryption/Decryption
        ciphertext = test_encryption_decryption(plaintext, key, iv)
        print()
        print_info(f"Ciphertext (hex):")
        print(f"  {format_hex(ciphertext)}")
        print()

        # Test 4: Last byte recovery
        test_last_byte_recovery(plaintext, key, iv, ciphertext)
        print()

        # Test 5: Full attack
        recovered = test_full_oracle_attack(plaintext, key, iv, ciphertext)
        print()

        # Test 6: Verify success
        success = verify_attack_success(plaintext, recovered)
        print()

        if success:
            print_header("ALL TESTS PASSED!")
            print(f"""
{Colors.GREEN}{Colors.BOLD}SUCCESS!{Colors.END}

The padding oracle attack successfully recovered the plaintext.

{Colors.BOLD}Key Findings:{Colors.END}
  ✓ Original message: {plaintext}
  ✓ Encrypted with AES-CBC
  ✓ Key was: {key.hex()[:32]}... (SECRET)
  ✓ Attack recovered plaintext WITHOUT knowing the key
  ✓ Vulnerability demonstrated successfully

{Colors.BOLD}Security Implications:{Colors.END}
  • Even strong encryption (AES-CBC) can be broken
  • If server reveals padding validity, attackers can recover plaintext
  • Solution: Use authenticated encryption (AES-GCM)
  • Or: Use encrypt-then-MAC to prevent oracle attacks
""")
        else:
            print_error("TESTS FAILED - Attack did not recover correct plaintext")

        input(f"\n{Colors.YELLOW}Press Enter to return to menu...{Colors.END}")
        return success

    except Exception as e:
        print_error(f"Test error: {e}")
        import traceback
        traceback.print_exc()
        input(f"\n{Colors.YELLOW}Press Enter to return to menu...{Colors.END}")
        return False


def run_performance_benchmark():
    """Run performance benchmarks"""
    print_header("PERFORMANCE BENCHMARK")

    test_cases = [
        (b"A", "1 byte"),
        (b"A" * 8, "8 bytes"),
        (b"A" * 15, "15 bytes"),
        (b"A" * 16, "16 bytes (full block)"),
    ]

    print("Message Length | Recovery Time | Queries (est.)")
    print("-" * 50)

    for plaintext, desc in test_cases:
        key = os.urandom(16)
        iv = os.urandom(16)
        ciphertext = encrypt_cbc(plaintext, key, iv)

        start = time.time()
        recovered = recover_plaintext(key, iv, ciphertext)
        elapsed = time.time() - start

        # Estimate queries (roughly 256 per byte, but varies)
        estimated_queries = len(plaintext) * 256

        print(f"{desc:14} | {elapsed:13.2f}s | {estimated_queries:13}")

    print()


if __name__ == '__main__':
    try:
        run_test_suite_interactive()
    except KeyboardInterrupt:
        print(f"\n\n{Colors.YELLOW}Program interrupted by user{Colors.END}")
        sys.exit(0)
    except Exception as e:
        print_error(f"Unexpected error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
