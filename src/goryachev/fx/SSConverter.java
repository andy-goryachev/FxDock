// Copyright © 2016-2019 Andy Goryachev <andy@goryachev.com>
package goryachev.fx;
import goryachev.common.util.SStream;


/**
 * String Stream Converter.
 */
public interface SSConverter<T>
{
    public abstract SStream toStream(T object);


    public abstract T fromStream(SStream s);
}
