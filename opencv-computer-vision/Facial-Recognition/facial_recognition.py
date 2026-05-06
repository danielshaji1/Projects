"""
Face/ Eye detection from webcam of choice
"""


import cv2
import numpy as np


cap = cv2.VideoCapture(0)

#haar cascade, already knows what it's looking for and looks for specific feautres in that image, (pre-trained)
face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
eye_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_eye.xml')

while True:
    ret, frame = cap.read()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    '''
    detectMultiScale(scale_factor=shrinking images down to match the templates at which the cascades were trained_smaller is higher accuracy but slower performing and vice verse,
    minNeighbors=how many candidates to confirm before determining that something is actually a face:at least _blank_ rectangles before confirming what is a face and what isn't,
    minSize=min size of a face, max_size=max size of a face these two are optional.)
    '''
    faces = face_cascade.detectMultiScale(gray, 1.3, 5)

    for (x, y, w, h) in faces:
        cv2.rectangle(frame, (x, y), (x + w, y + h), (255, 0, 0 ), 5)
        roi_gray = gray[y:y+w, x:x+w]
        roi_color = frame[y:y+w, x:x+w]
        eyes = eye_cascade.detectMultiScale(roi_gray, 1.3, 6)
        for (ex, ey, ew, eh) in eyes:
            cv2.rectangle(roi_color, (ex, ey), (ex + ew, ey + eh), (0, 255, 0))

    cv2.imshow('frame', frame)

    if cv2.waitKey(1) == ord('q'):
        break


cap.release()
cv2.destroyAllWindows()
