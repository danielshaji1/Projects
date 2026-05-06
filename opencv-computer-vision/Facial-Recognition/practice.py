import cv2


img = cv2.imread('digital_camera_photo-1080x675.jpg')


cv2.imshow('Image', img)
cv2.waitKey(0)
cv2.destroyAllWindows()