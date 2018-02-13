package com.xiaojigou.luo.camfilter;

import java.nio.FloatBuffer;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.xiaojigou.luo.camfilter.utils.OpenGlUtils;

public class LuoGPUCameraInputFilter extends LuoGPUImgBaseFilter {

    public static final String CAMERA_INPUT_FILTER_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "uniform mat4 textureTransform;\n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = (textureTransform * inputTextureCoordinate).xy;\n" +
            "}";

    public static final String CAMERA_INPUT_FILTER_FRAGMENT_SHADER =
            "#extension GL_OES_EGL_image_external : require\n" +
            "precision mediump float;\n" +
            "varying mediump vec2 textureCoordinate;\n" +
            "uniform samplerExternalOES inputImageTexture;\n" +
            "void main()\n" +
            "{\n" +
            "gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "}\n";

    private float[] mTextureTransformMatrix;
    private int mTextureTransformMatrixLocation;

    public LuoGPUCameraInputFilter(){
        super(CAMERA_INPUT_FILTER_VERTEX_SHADER ,
                CAMERA_INPUT_FILTER_FRAGMENT_SHADER);
    }

    protected void onInit() {
        super.onInit();
        mTextureTransformMatrixLocation = GLES20.glGetUniformLocation(mGLProgId, "textureTransform");

    }

    public void setTextureTransformMatrix(float[] mtx){
        mTextureTransformMatrix = mtx;
    }



    @Override
    public int onDrawFrame(int textureId, FloatBuffer vertexBuffer, FloatBuffer textureBuffer) {
        GLES20.glUseProgram(mGLProgId);
        runPendingOnDrawTasks();
        if(!isInitialized()) {
            return OpenGlUtils.NOT_INIT;
        }
        vertexBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribPosition, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribPosition);
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glUniformMatrix4fv(mTextureTransformMatrixLocation, 1, false, mTextureTransformMatrix, 0);

        if(textureId != OpenGlUtils.NO_TEXTURE){
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
            GLES20.glUniform1i(mGLUniformTexture, 0);
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return OpenGlUtils.ON_DRAWN;
    }



    private int[] mFrameBuffers = null;
    private int[] mFrameBufferTextures = null;
    private int mFrameWidth = -1;
    private int mFrameHeight = -1;
    public int onDrawToTexture(final int textureId) {
        if(mFrameBuffers == null)
            return OpenGlUtils.NO_TEXTURE;
        runPendingOnDrawTasks();
        GLES20.glViewport(0, 0, mFrameWidth, mFrameHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
        onDrawFrame(textureId,mGLCubeBuffer,mGLTextureBuffer);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glViewport(0, 0, mFrameWidth, mFrameHeight);
        return mFrameBufferTextures[0];
    }
    public int onDrawToTexture(final int textureId,FloatBuffer vertexBuffer, FloatBuffer textureBuffer) {
        if(mFrameBuffers == null)
            return OpenGlUtils.NO_TEXTURE;
        runPendingOnDrawTasks();
        GLES20.glViewport(0, 0, mFrameWidth, mFrameHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
        onDrawFrame(textureId,vertexBuffer,textureBuffer);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        GLES20.glViewport(0, 0, mFrameWidth, mFrameHeight);
        return mFrameBufferTextures[0];
    }




    public void initCameraFrameBuffer(int width, int height) {
        if(mFrameBuffers != null && (mFrameWidth != width || mFrameHeight != height))
            destroyFramebuffers();
        if (mFrameBuffers == null) {
            mFrameWidth = width;
            mFrameHeight = height;
            mFrameBuffers = new int[1];
            mFrameBufferTextures = new int[1];

            GLES20.glGenFramebuffers(1, mFrameBuffers, 0);
            GLES20.glGenTextures(1, mFrameBufferTextures, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
                    GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
                    GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFrameBuffers[0]);
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                    GLES20.GL_TEXTURE_2D, mFrameBufferTextures[0], 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        }
    }

    public void destroyFramebuffers() {
        if (mFrameBufferTextures != null) {
            GLES20.glDeleteTextures(1, mFrameBufferTextures, 0);
            mFrameBufferTextures = null;
        }
        if (mFrameBuffers != null) {
            GLES20.glDeleteFramebuffers(1, mFrameBuffers, 0);
            mFrameBuffers = null;
        }
        mFrameWidth = -1;
        mFrameHeight = -1;
    }

    @Override
    public void onInputSizeChanged(final int width, final int height) {
        super.onInputSizeChanged(width, height);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyFramebuffers();
    }


}