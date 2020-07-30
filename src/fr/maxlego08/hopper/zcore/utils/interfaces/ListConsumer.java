package fr.maxlego08.hopper.zcore.utils.interfaces;

import java.util.Collection;

public interface ListConsumer<T> {

	Collection<String> accept(T t);
	
}
