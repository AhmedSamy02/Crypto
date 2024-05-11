import base64

def read_audio_file_as_ascii(file_path):
    with open(file_path, 'rb') as file:
        audio_bytes = file.read()
        ascii_representation = ''.join([chr(byte) for byte in audio_bytes])
        return ascii_representation

# File path to the audio file
audio_file_path = "lastcall.wav"

# Read the audio file and get its ASCII representation
ascii_audio_text = read_audio_file_as_ascii(audio_file_path)

# Print the ASCII representation
print("ASCII representation of audio file:", ascii_audio_text)
