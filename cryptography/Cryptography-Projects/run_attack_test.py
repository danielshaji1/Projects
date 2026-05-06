import os
from padding import encrypt_cbc, decrypt_cbc, recover_plaintext

test_messages = [b'A', b'TEST', b'Hello!']
success_count = 0

for msg in test_messages:
    key = os.urandom(16)
    iv = os.urandom(16)

    ciphertext = encrypt_cbc(msg, key, iv)
    recovered = recover_plaintext(key, iv, ciphertext)

    if recovered and len(recovered) > 0:
        padding_len = recovered[-1]
        if padding_len <= len(recovered) and padding_len <= 16:
            recovered_clean = recovered[:-padding_len]
        else:
            recovered_clean = recovered
    else:
        recovered_clean = b''

    if recovered_clean == msg:
        print(f'✓ {msg!r} - SUCCESS')
        success_count += 1
    else:
        print(f'✗ {msg!r} - FAILED (got {recovered_clean!r})')

print(f'\nTotal: {success_count}/{len(test_messages)} successful')
