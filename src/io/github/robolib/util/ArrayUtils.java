/*
 * Copyright (c) 2015-2020 noriah <vix@noriah.dev>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 */

package io.github.robolib.util;

import java.lang.reflect.Array;

/**
 *
 *
 * @author noriah <vix@noriah.dev>
 */
public class ArrayUtils {

    @SuppressWarnings("unchecked")
    public static final <T> T[] concatenate(T[] a, T[] b) {
        int l1 = a.length, l2 = b.length;
        T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), l1 + l2);
        System.arraycopy(a, 0, c, 0, l1);
        System.arraycopy(b, 0, c, l1, l2);
        return c;
    }

}
