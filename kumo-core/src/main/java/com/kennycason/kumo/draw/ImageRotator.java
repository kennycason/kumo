package com.kennycason.kumo.draw;

/**
 * Created by kenny on 6/29/14.
 */
public class ImageRotator implements IImageRotator{

    private IImageRotator platformSpecificImplementation;

    public ImageRotator(){
        String name = "ImageRotatorImpl";

        try {

            Class<? extends IImageRotator> clazz = (Class<? extends IImageRotator>) Class.forName(Platform.PACKAGE + name);
            platformSpecificImplementation = clazz.newInstance();

        } catch(ClassNotFoundException | IllegalAccessException | InstantiationException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public Image rotate(IImage image, double theta) {
        return new Image(platformSpecificImplementation.rotate(image, theta));
    }
}
