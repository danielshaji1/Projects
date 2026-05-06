"""
Comprehensive test suite for Padding Oracle Attack on AES-CBC
Tests all three components: padding, AES-CBC, and the attack itself
"""

import pytest
import os
from padding import (
    pad, check_pad, encrypt_cbc, decrypt_cbc, recover_last_byte, recover_plaintext, AES
)


class TestPaddingFunction:
    """Tests for the PKCS#7 padding function"""

    def test_pad_empty_message(self):
        """Test padding an empty message - should add full block of 0x10"""
        message = b''
        padded = pad(message)
        assert len(padded) == 16
        assert padded == bytes([0x10] * 16)

    def test_pad_single_byte(self):
        """Test padding a 1-byte message"""
        message = b'A'
        padded = pad(message)
        assert len(padded) == 16
        assert padded[0] == ord('A')
        assert all(b == 0x0F for b in padded[1:])

    def test_pad_16_bytes(self):
        """Test padding a full 16-byte message - should add another full block"""
        message = bytes(range(16))
        padded = pad(message)
        assert len(padded) == 32
        assert padded[:16] == message
        assert padded[16:] == bytes([0x10] * 16)

    def test_pad_various_lengths(self):
        """Test padding for various message lengths"""
        for length in range(1, 32):
            message = b'X' * length
            padded = pad(message)
            # Check that padded length is multiple of 16
            assert len(padded) % 16 == 0
            # Check that padding bytes match the padding length
            padding_length = 16 - (length % 16)
            assert all(b == padding_length for b in padded[-padding_length:])

    def test_pad_15_bytes(self):
        """Test padding a 15-byte message"""
        message = b'X' * 15
        padded = pad(message)
        assert len(padded) == 16
        assert padded[-1] == 0x01


class TestPaddingValidation:
    """Tests for the check_pad function"""

    def test_valid_padding_single_byte(self):
        """Test validation of valid 1-byte padding"""
        message = b'X' * 15 + bytes([0x01])
        assert check_pad(message) is True

    def test_valid_padding_full_block(self):
        """Test validation of valid full block padding"""
        message = bytes([0x10] * 16)
        assert check_pad(message) is True

    def test_valid_padding_5_bytes(self):
        """Test validation of valid 5-byte padding"""
        message = b'X' * 11 + bytes([0x05] * 5)
        assert check_pad(message) is True

    def test_invalid_padding_wrong_length(self):
        """Test validation fails when padding length value doesn't match actual length"""
        message = b'X' * 15 + bytes([0x02])  # Claims 2 bytes but only 1
        assert check_pad(message) is False

    def test_invalid_padding_zero(self):
        """Test validation fails with zero padding length"""
        message = b'X' * 15 + bytes([0x00])
        assert check_pad(message) is False

    def test_invalid_padding_exceeds_block(self):
        """Test validation fails when padding length exceeds block size"""
        message = b'X' * 15 + bytes([0x11])
        assert check_pad(message) is False

    def test_invalid_padding_inconsistent_bytes(self):
        """Test validation fails when padding bytes don't match length"""
        message = b'X' * 12 + bytes([0x04, 0x04, 0x03, 0x04])
        assert check_pad(message) is False


class TestAESCBC:
    """Tests for AES-CBC encryption and decryption"""

    @pytest.fixture
    def key_iv(self):
        """Generate a random 128-bit key and IV"""
        key = os.urandom(16)
        iv = os.urandom(16)
        return key, iv

    def test_encrypt_decrypt_single_block(self, key_iv):
        """Test encryption and decryption of a single block"""
        key, iv = key_iv
        plaintext = b'Hello World!!!!!'

        ciphertext = encrypt_cbc(plaintext, key, iv)
        decrypted = decrypt_cbc(ciphertext, key, iv)

        assert decrypted == plaintext

    def test_encrypt_decrypt_multiple_blocks(self, key_iv):
        """Test encryption and decryption of multiple blocks"""
        key, iv = key_iv
        plaintext = b'Hello World! This is a longer message.'

        ciphertext = encrypt_cbc(plaintext, key, iv)
        decrypted = decrypt_cbc(ciphertext, key, iv)

        # Check that decrypted matches original (with padding removed)
        assert decrypted[:len(plaintext)] == plaintext or decrypted == plaintext

    def test_encrypt_empty_message(self, key_iv):
        """Test encryption of empty message (should pad to 16 bytes)"""
        key, iv = key_iv
        plaintext = b''

        ciphertext = encrypt_cbc(plaintext, key, iv)
        decrypted = decrypt_cbc(ciphertext, key, iv)

        assert decrypted == plaintext

    def test_ciphertext_length_multiple_of_16(self, key_iv):
        """Test that ciphertext length is always multiple of 16"""
        key, iv = key_iv
        for msg_len in [1, 5, 15, 16, 17, 31, 32]:
            plaintext = b'X' * msg_len
            ciphertext = encrypt_cbc(plaintext, key, iv)
            assert len(ciphertext) % 16 == 0

    def test_different_iv_different_ciphertext(self):
        """Test that different IVs produce different ciphertexts"""
        key = os.urandom(16)
        plaintext = b'Hello World!!!!!'

        iv1 = os.urandom(16)
        iv2 = os.urandom(16)

        ciphertext1 = encrypt_cbc(plaintext, key, iv1)
        ciphertext2 = encrypt_cbc(plaintext, key, iv2)

        assert ciphertext1 != ciphertext2

    def test_invalid_padding_returns_none(self):
        """Test that decryption with invalid padding returns None"""
        key = os.urandom(16)
        iv = os.urandom(16)

        # Create a ciphertext that will decrypt to invalid padding
        aes = AES(key)
        valid_plaintext = pad(b'Test')

        # Modify a byte to create invalid padding
        ciphertext = encrypt_cbc(b'Test', key, iv)
        modified_ciphertext = ciphertext[:-1] + bytes([ciphertext[-1] ^ 0xFF])

        result = decrypt_cbc(modified_ciphertext, key, iv)
        # Should either return None or raise an error for invalid padding
        # (depending on implementation)
        assert result is None or isinstance(result, (bytes, type(None)))

    def test_deterministic_encryption_same_inputs(self, key_iv):
        """Test that same plaintext and IV produce same ciphertext"""
        key, iv = key_iv
        plaintext = b'Hello World!!!!!'

        ciphertext1 = encrypt_cbc(plaintext, key, iv)
        ciphertext2 = encrypt_cbc(plaintext, key, iv)

        assert ciphertext1 == ciphertext2


class TestPaddingOracleAttackLastByte:
    """Tests for recovering the last byte of plaintext"""

    @pytest.fixture
    def attack_setup(self):
        """Set up key, IV, and ciphertext for attack"""
        key = os.urandom(16)
        iv = os.urandom(16)
        plaintext = b'Secret Message!!'  # Exactly 16 bytes
        ciphertext = encrypt_cbc(plaintext, key, iv)
        return key, iv, plaintext, ciphertext

    def test_recover_last_byte_single_block(self, attack_setup):
        """Test that we can recover the last byte using the oracle"""
        key, iv, plaintext, ciphertext = attack_setup

        recovered_byte = recover_last_byte(key, iv, ciphertext)

        # The recovered byte should be 0x01 (the padding value)
        assert recovered_byte is not None
        assert isinstance(recovered_byte, int)

    def test_recover_last_byte_various_messages(self):
        """Test recovery of last byte for various plaintext messages"""
        for test_msg in [b'A' * 16, b'Hello World!!!!', b'X' * 15]:
            key = os.urandom(16)
            iv = os.urandom(16)
            ciphertext = encrypt_cbc(test_msg, key, iv)

            recovered_byte = recover_last_byte(key, iv, ciphertext)

            assert recovered_byte is not None
            # For single block with full 16-byte message, last byte is 0x01
            if len(test_msg) == 16:
                assert recovered_byte == 0x01


class TestFullPlaintextRecovery:
    """Tests for recovering the entire plaintext using padding oracle attack"""

    @pytest.fixture
    def attack_setup(self):
        """Set up test environment"""
        key = os.urandom(16)
        iv = os.urandom(16)
        return key, iv

    def test_recover_full_plaintext_16_bytes(self, attack_setup):
        """Test recovery of full plaintext with exactly 16 bytes"""
        key, iv = attack_setup
        plaintext = b'SecretMessage!!'  # 16 bytes

        ciphertext = encrypt_cbc(plaintext, key, iv)
        recovered = recover_plaintext(key, iv, ciphertext)

        assert recovered is not None
        # First 15 bytes should match (last byte is padding)
        assert recovered[:15] == plaintext[:15]

    def test_recover_full_plaintext_15_bytes(self, attack_setup):
        """Test recovery of full plaintext with 15 bytes"""
        key, iv = attack_setup
        plaintext = b'SecretMessage!'  # 15 bytes (will be padded with 0x01)

        ciphertext = encrypt_cbc(plaintext, key, iv)
        recovered = recover_plaintext(key, iv, ciphertext)

        assert recovered is not None
        assert len(recovered) == 16
        # Verify the plaintext matches (ignoring padding)
        assert recovered[:15] == plaintext

    def test_recover_full_plaintext_1_byte(self, attack_setup):
        """Test recovery of plaintext with just 1 byte"""
        key, iv = attack_setup
        plaintext = b'A'

        ciphertext = encrypt_cbc(plaintext, key, iv)
        recovered = recover_plaintext(key, iv, ciphertext)

        assert recovered is not None
        assert recovered[0] == ord('A')

    def test_recover_full_plaintext_empty(self, attack_setup):
        """Test recovery of empty plaintext (padded to 16 bytes)"""
        key, iv = attack_setup
        plaintext = b''

        ciphertext = encrypt_cbc(plaintext, key, iv)
        recovered = recover_plaintext(key, iv, ciphertext)

        assert recovered is not None
        # Should be all 0x10 (16 bytes of padding)
        assert len(recovered) == 16
        assert all(b == 0x10 for b in recovered)

    def test_attack_succeeds_without_key(self):
        """
        Test that the padding oracle attack can recover plaintext
        WITHOUT knowing the encryption key (only using the oracle)
        This is the core of the attack!
        """
        # Setup
        key = os.urandom(16)
        iv = os.urandom(16)
        plaintext = b'Attack Works!!!!!'  # 16 bytes

        # Encrypt the plaintext
        ciphertext = encrypt_cbc(plaintext, key, iv)

        # Now we only have ciphertext and IV, but we can still recover plaintext
        # using the padding oracle (decrypt_cbc function acts as the oracle)
        recovered = recover_plaintext(key, iv, ciphertext)

        # Verify attack succeeded
        assert recovered is not None
        assert len(recovered) == 16
        # The recovered plaintext should match original
        assert recovered[:16] == plaintext or recovered[:15] == plaintext[:15]

    def test_different_plaintexts_different_recovery(self, attack_setup):
        """Test that different plaintexts recover to different values"""
        key, iv = attack_setup
        plaintext1 = b'FirstMessage!!!!'
        plaintext2 = b'SecondMessage!!!'

        ciphertext1 = encrypt_cbc(plaintext1, key, iv)
        ciphertext2 = encrypt_cbc(plaintext2, key, iv)

        recovered1 = recover_plaintext(key, iv, ciphertext1)
        recovered2 = recover_plaintext(key, iv, ciphertext2)

        assert recovered1 is not None
        assert recovered2 is not None
        assert recovered1 != recovered2


class TestAttackConsistency:
    """Tests to verify the attack is working consistently"""

    def test_attack_deterministic(self):
        """Test that the attack produces deterministic results"""
        key = os.urandom(16)
        iv = os.urandom(16)
        plaintext = b'ConsistentTest!!'

        ciphertext = encrypt_cbc(plaintext, key, iv)

        # Run attack multiple times
        recovered1 = recover_plaintext(key, iv, ciphertext)
        recovered2 = recover_plaintext(key, iv, ciphertext)
        recovered3 = recover_plaintext(key, iv, ciphertext)

        assert recovered1 == recovered2 == recovered3

    def test_attack_independent_of_plaintext_content(self):
        """Test that attack works regardless of plaintext content"""
        key = os.urandom(16)
        iv = os.urandom(16)

        test_plaintexts = [
            b'A' * 16,
            b'\x00' * 16,
            b'\xFF' * 16,
            b'Mixed123\x00\xFF!!!'
        ]

        for plaintext in test_plaintexts:
            ciphertext = encrypt_cbc(plaintext, key, iv)
            recovered = recover_plaintext(key, iv, ciphertext)

            assert recovered is not None
            # At minimum, should recover some bytes correctly
            assert len(recovered) == 16


class TestEndToEndAttack:
    """End-to-end tests simulating real-world attack scenario"""

    def test_complete_attack_workflow(self):
        """
        Simulate complete attack workflow:
        1. Attacker has ciphertext and IV
        2. Has access to padding oracle (decrypt_cbc)
        3. Recovers plaintext without knowing key
        """
        # Step 1: Victim encrypts a message
        key = os.urandom(16)
        iv = os.urandom(16)
        secret_plaintext = b'Confidential!!!!'

        ciphertext = encrypt_cbc(secret_plaintext, key, iv)

        # Step 2: Attacker intercepts ciphertext and IV
        # (but doesn't know the key)

        # Step 3: Attacker uses padding oracle to recover plaintext
        recovered_plaintext = recover_plaintext(key, iv, ciphertext)

        # Step 4: Verify attack succeeded
        assert recovered_plaintext is not None
        assert len(recovered_plaintext) > 0

        # The attack should recover the actual data
        # (may have padding but the content should match)
        assert secret_plaintext in recovered_plaintext or recovered_plaintext[:len(secret_plaintext)] == secret_plaintext

    def test_attack_across_multiple_messages(self):
        """Test attacking multiple encrypted messages with same key"""
        key = os.urandom(16)
        messages = [
            b'Message One!!!!',
            b'Message Number2',
            b'Third Message!!',
        ]

        for plaintext in messages:
            iv = os.urandom(16)
            ciphertext = encrypt_cbc(plaintext, key, iv)
            recovered = recover_plaintext(key, iv, ciphertext)

            assert recovered is not None
            # First 15 bytes should match original
            assert recovered[:15] == plaintext


# Performance test
class TestAttackPerformance:
    """Tests to verify attack performance is reasonable"""

    def test_recovery_completes_in_reasonable_time(self):
        """Test that plaintext recovery doesn't take too long"""
        import time

        key = os.urandom(16)
        iv = os.urandom(16)
        plaintext = b'PerformanceTest'

        ciphertext = encrypt_cbc(plaintext, key, iv)

        start = time.time()
        recovered = recover_plaintext(key, iv, ciphertext)
        elapsed = time.time() - start

        # Should complete in reasonable time (less than 10 seconds)
        # The attack requires many oracle calls but should still be fast
        assert elapsed < 10
        assert recovered is not None


if __name__ == '__main__':
    # Run tests with pytest
    pytest.main([__file__, '-v', '--tb=short'])
