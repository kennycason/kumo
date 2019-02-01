package com.kennycason.kumo.abst;

/**
 * Created by kenny on 6/29/14.
 */
public abstract class ImageRotatorAbst {

    public abstract ImageAbst rotate(final ImageAbst image, final double theta);

    public static ImageRotatorAbst get(){
        String name = "ImageRotatorImpl";

        try {

            Class<? extends ImageRotatorAbst> clazz = (Class<? extends ImageRotatorAbst>) Class.forName(Platform.PACKAGE + name);
            return clazz.newInstance();

        } catch(ClassNotFoundException | IllegalAccessException | InstantiationException ex){
            ex.printStackTrace();
        }
        return null;
    }
}
