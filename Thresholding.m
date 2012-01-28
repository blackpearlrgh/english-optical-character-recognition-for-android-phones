f=imread('colors.jpg');
%imshow(f);
T=0.5*(double(min(f(:)))+double(max(f(:))));
done=false;
while ~done
    g=f>=T;
    Tnext=0.5*(mean(f(g))+mean(f(~g)));
    done=abs(T-Tnext)<0.5;
    T=Tnext;
end
%M=graythresh(f);
%imshow(M);
imshow(f);