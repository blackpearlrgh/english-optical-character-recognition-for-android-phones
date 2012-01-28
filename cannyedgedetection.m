
myimage=imread('A4cube.jpg');
%imshow(myimage); % 3shan tshofo el pic :D
testgray=rgb2gray(myimage);
T=graythresh(testgray);
bw=im2bw(testgray, T);
%imshow(bw);  %image binarized 

%T=[0.1 0.5];
T=graythresh(myimage);
[BW,t] = edge(bw,'canny',T,0.5);
%[BW,t] = edge(bw,'canny');
%gboth=edge(bw,'sobel',0.15);
imshow(gboth);

