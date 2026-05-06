import threading

from deepface import DeepFace
import cv2

cap = cv2.VideoCapture(0, cv2.CAP_DSHOW)

cap.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)

counter = 0

face_match = False

referemce_img = cv2.imread("human_face.jpg")

while True:
    ret, frame = cap.read()
    