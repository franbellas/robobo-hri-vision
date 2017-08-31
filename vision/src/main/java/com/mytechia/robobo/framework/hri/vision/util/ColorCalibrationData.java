/**
 * *****************************************************************************
 * <p>
 * Copyright (C) 2017 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 * Copyright (C) 2017 Luis Llamas <luis.llamas@mytechia.com>
 * <p>
 * This file is part of Robobo App Setup.
 * ****************************************************************************
 */
package com.mytechia.robobo.framework.hri.vision.util;

import java.io.Serializable;

/**
 * HSV Range of a color
 *
 * @author Luis Llamas <luis.llamas@mytechia.com>.
 */
public class ColorCalibrationData implements Serializable {
    private int minH;
    private int minS;
    private int minV;
    private int maxH;
    private int maxS;
    private int maxV;

    public ColorCalibrationData(int minH, int minS, int minV, int maxH, int maxS, int maxV){
        this.minH = minH;
        this.minS = minS;
        this.minV = minV;
        this.maxH = maxH;
        this.maxS = maxS;
        this.maxV = maxV;
    }
    public int getMinH() {
        return this.minH;
    }

    public int getMinS() {
        return this.minS;
    }

    public int getMinV() {
        return this.minV;
    }

    public int getMaxH() {
        return this.maxH;
    }

    public int getMaxS() {
        return this.maxS;
    }

    public int getMaxV() {
        return this.maxV;
    }
}
