package cat.uvic.android;
/**
* @author ANNA
*
*/
public class ImageException extends Exception {
/**
*
*/
private static final long serialVersionUID = 1L;
/**
*
*/
public ImageException() {
super();
}
/**
* @param arg0
* @param arg1
*/
public ImageException(String arg0, Throwable arg1) {
super(arg0, arg1);
}
/**
* @param arg0
*/
public ImageException(String arg0) {
super(arg0);
}
/**
* @param arg0
*/
public ImageException(Throwable arg0) {
super(arg0);
}
}