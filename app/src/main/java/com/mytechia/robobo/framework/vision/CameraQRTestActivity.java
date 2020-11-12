/*******************************************************************************
 *
 *   Copyright 2018 Mytech Ingenieria Aplicada <http://www.mytechia.com>
 *   Copyright 2018 Luis Llamas <luis.llamas@mytechia.com>
 *
 *   This file is part of Robobo Vision Modules.
 *
 *   Robobo Vision Modules is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Robobo Remote Control Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Robobo Vision Modules.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/
package com.mytechia.robobo.framework.vision;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.core.view.GestureDetectorCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mytechia.robobo.framework.RoboboManager;
import com.mytechia.robobo.framework.exception.ModuleNotFoundException;
import com.mytechia.robobo.framework.hri.vision.basicCamera.Frame;
import com.mytechia.robobo.framework.hri.vision.basicCamera.ICameraListener;
import com.mytechia.robobo.framework.hri.vision.basicCamera.ICameraModule;
import com.mytechia.robobo.framework.hri.vision.qrTracking.IQRListener;
import com.mytechia.robobo.framework.hri.vision.qrTracking.IQRTrackingModule;
import com.mytechia.robobo.framework.hri.vision.qrTracking.QRInfo;
import com.mytechia.robobo.framework.service.RoboboServiceHelper;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;

import static org.opencv.android.CameraBridgeViewBase.CAMERA_ID_FRONT;

public class CameraQRTestActivity extends AppCompatActivity implements ICameraListener, IQRListener, GestureDetector.OnGestureListener{
    private static final String TAG="CameraQRTestActivity";


    private GestureDetectorCompat mDetector;

    private RoboboServiceHelper roboboHelper;
    private RoboboManager roboboManager;


    private ICameraModule camModule;
    private CameraBridgeViewBase bridgeBase;
    private IQRTrackingModule qrModule;

    private RelativeLayout rellayout = null;
    private TextView textView = null;
    private SurfaceView surfaceView = null;
    private ImageView imageView = null;
    private TextureView textureView = null;
    private Frame actualFrame ;

    private Frame lastFrame;
    private boolean paused = true;
    private long lastDetection = 0;
    private int index = CAMERA_ID_FRONT;

    private boolean lost = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,"TouchEvent");

       this.mDetector.onTouchEvent(event);
        return true;

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_test);




        //this.surfaceView = (SurfaceView) findViewById(R.id.testSurfaceView);
        this.imageView = (ImageView) findViewById(R.id.testImageView) ;
        this.bridgeBase = (CameraBridgeViewBase) findViewById(R.id.HelloOpenCvView);

//        this.textureView = (TextureView) findViewById(R.id.textureView);
        roboboHelper = new RoboboServiceHelper(this, new RoboboServiceHelper.Listener() {
            @Override
            public void onRoboboManagerStarted(RoboboManager robobo) {

                //the robobo service and manager have been started up
                roboboManager = robobo;


                //dismiss the wait dialog


                //start the "custom" robobo application
                startRoboboApplication();

            }

            @Override
            public void onError(Throwable errorMsg) {

                final String error = errorMsg.getLocalizedMessage();


            }

        });

        //start & bind the Robobo service
        Bundle options = new Bundle();
        roboboHelper.bindRoboboService(options);
    }
    private void startRoboboApplication() {

        try {

            this.camModule = this.roboboManager.getModuleInstance(ICameraModule.class);
            this.qrModule = this.roboboManager.getModuleInstance(IQRTrackingModule.class);


        } catch (ModuleNotFoundException e) {
            e.printStackTrace();
        }


        //camModule.passSurfaceView(surfaceView);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                bridgeBase.setVisibility(SurfaceView.VISIBLE);
                camModule.passOCVthings(bridgeBase);



                camModule.signalInit();


            }
        });
        mDetector = new GestureDetectorCompat(getApplicationContext(),this);
        camModule.suscribe(this);
        qrModule.suscribe(this);




    }

    @Override
    public void onNewFrame(final Frame frame) {


        lastFrame = frame;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (lost){
                    imageView.setImageBitmap(frame.getBitmap());

                }


            }
        });

    }

    @Override
    public void onNewMat(Mat mat) {

    }

    @Override
    public void onDebugFrame(Frame frame, String frameId) {

    }

    @Override
    public void onOpenCVStartup() {

    }



    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        camModule.changeCamera();

        return false;
    }

    @Override
    public void onQRDetected(final QRInfo qr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Bitmap tempBitmap =lastFrame.getBitmap().copy(Bitmap.Config.ARGB_8888,true);
                Canvas c = new Canvas(tempBitmap);



                Paint paint = new Paint();
                paint.setColor(Color.GREEN);
                Paint paintBlue = new Paint();
                paintBlue.setColor(Color.BLUE);
                Paint paintRed = new Paint();
                paintRed.setColor(Color.RED);
                Paint paintYellow = new Paint();
                paintYellow.setColor(Color.YELLOW);
                //imageView.setImageBitmap(lastFrame.getBitmap());
                c.drawCircle((int)qr.getxPosition(),(int)qr.getyPosition(),5 ,paint);

                c.drawText(qr.getIdString(),qr.getxPosition()+10,qr.getyPosition()-5 ,paint);
                imageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

            }
        });
    }

    @Override
    public void onQRAppears(QRInfo qr) {
        lost = false;
    }

    @Override
    public void onQRDisappears(QRInfo qr) {
        lost = true;
    }
}
