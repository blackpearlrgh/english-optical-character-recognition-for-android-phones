
clc, close all
imagen=imread('abcCapital.jpg');
imshow(imagen);
imagen=rgb2gray(imagen);
imagen =~im2bw(imagen,graythresh(imagen));
imagen = bwareaopen(imagen,30);
re=imagen;
while 1
    %Fcn 'lines' separate lines in text
    [fl re]=lines(re);
    imgn=fl;
      
    % Label and count connected components
    [L Ne] = bwlabel(imgn);    
    for n=1:Ne
        [r,c] = find(L==n);
        % Extract letter
        n1=imgn(min(r):max(r),min(c):max(c));  
        figure, imshow(n1);
       
    end
    %*When the sentences finish, breaks the loop
    if isempty(re)  %See variable 're' in Fcn 'lines'
        break
    end    
end

clear all