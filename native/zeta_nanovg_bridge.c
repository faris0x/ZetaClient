#include <jni.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

#define GL_GLEXT_PROTOTYPES 1
#include <GL/gl.h>
#include <GL/glext.h>

#define NANOVG_GL2_IMPLEMENTATION
#include "nanovg.h"
#include "nanovg_gl.h"

static NVGcontext* g_ctx = NULL;

JNIEXPORT jlong JNICALL
Java_zeta_client_nanovg_NanoVG_nvgCreate(JNIEnv* env, jclass cls, jint flags) {
    g_ctx = nvgCreateGL2(flags);
    return (jlong)(uintptr_t)g_ctx;
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgDelete(JNIEnv* env, jclass cls, jlong ctx) {
    NVGcontext* c = (NVGcontext*)(uintptr_t)ctx;
    if (c) nvgDeleteGL2(c);
    if (c == g_ctx) g_ctx = NULL;
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgBeginFrame(JNIEnv* env, jclass cls, jlong ctx, jint w, jint h, jfloat ratio) {
    nvgBeginFrame((NVGcontext*)(uintptr_t)ctx, (float)w, (float)h, ratio);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgEndFrame(JNIEnv* env, jclass cls, jlong ctx) {
    nvgEndFrame((NVGcontext*)(uintptr_t)ctx);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgBeginPath(JNIEnv* env, jclass cls, jlong ctx) {
    nvgBeginPath((NVGcontext*)(uintptr_t)ctx);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgRect(JNIEnv* env, jclass cls, jlong ctx, jfloat x, jfloat y, jfloat w, jfloat h) {
    nvgRect((NVGcontext*)(uintptr_t)ctx, x, y, w, h);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgRoundedRect(JNIEnv* env, jclass cls, jlong ctx, jfloat x, jfloat y, jfloat w, jfloat h, jfloat r) {
    nvgRoundedRect((NVGcontext*)(uintptr_t)ctx, x, y, w, h, r);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgCircle(JNIEnv* env, jclass cls, jlong ctx, jfloat cx, jfloat cy, jfloat r) {
    nvgCircle((NVGcontext*)(uintptr_t)ctx, cx, cy, r);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgArc(JNIEnv* env, jclass cls, jlong ctx, jfloat cx, jfloat cy, jfloat r, jfloat a0, jfloat a1, jint dir) {
    nvgArc((NVGcontext*)(uintptr_t)ctx, cx, cy, r, a0, a1, dir);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgFill(JNIEnv* env, jclass cls, jlong ctx) {
    nvgFill((NVGcontext*)(uintptr_t)ctx);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgStroke(JNIEnv* env, jclass cls, jlong ctx) {
    nvgStroke((NVGcontext*)(uintptr_t)ctx);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgFillColor(JNIEnv* env, jclass cls, jlong ctx, jfloat r, jfloat g, jfloat b, jfloat a) {
    NVGcolor c = nvgRGBAf(r, g, b, a);
    nvgFillColor((NVGcontext*)(uintptr_t)ctx, c);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgStrokeColor(JNIEnv* env, jclass cls, jlong ctx, jfloat r, jfloat g, jfloat b, jfloat a) {
    NVGcolor c = nvgRGBAf(r, g, b, a);
    nvgStrokeColor((NVGcontext*)(uintptr_t)ctx, c);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgStrokeWidth(JNIEnv* env, jclass cls, jlong ctx, jfloat w) {
    nvgStrokeWidth((NVGcontext*)(uintptr_t)ctx, w);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgLineCap(JNIEnv* env, jclass cls, jlong ctx, jint cap) {
    nvgLineCap((NVGcontext*)(uintptr_t)ctx, cap);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgLineJoin(JNIEnv* env, jclass cls, jlong ctx, jint join) {
    nvgLineJoin((NVGcontext*)(uintptr_t)ctx, join);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgMiterLimit(JNIEnv* env, jclass cls, jlong ctx, jfloat limit) {
    nvgMiterLimit((NVGcontext*)(uintptr_t)ctx, limit);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgSave(JNIEnv* env, jclass cls, jlong ctx) {
    nvgSave((NVGcontext*)(uintptr_t)ctx);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgRestore(JNIEnv* env, jclass cls, jlong ctx) {
    nvgRestore((NVGcontext*)(uintptr_t)ctx);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgReset(JNIEnv* env, jclass cls, jlong ctx) {
    nvgReset((NVGcontext*)(uintptr_t)ctx);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgGlobalAlpha(JNIEnv* env, jclass cls, jlong ctx, jfloat alpha) {
    nvgGlobalAlpha((NVGcontext*)(uintptr_t)ctx, alpha);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgTranslate(JNIEnv* env, jclass cls, jlong ctx, jfloat x, jfloat y) {
    nvgTranslate((NVGcontext*)(uintptr_t)ctx, x, y);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgScale(JNIEnv* env, jclass cls, jlong ctx, jfloat x, jfloat y) {
    nvgScale((NVGcontext*)(uintptr_t)ctx, x, y);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgRotate(JNIEnv* env, jclass cls, jlong ctx, jfloat angle) {
    nvgRotate((NVGcontext*)(uintptr_t)ctx, angle);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgResetTransform(JNIEnv* env, jclass cls, jlong ctx) {
    nvgResetTransform((NVGcontext*)(uintptr_t)ctx);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgScissor(JNIEnv* env, jclass cls, jlong ctx, jfloat x, jfloat y, jfloat w, jfloat h) {
    nvgScissor((NVGcontext*)(uintptr_t)ctx, x, y, w, h);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgResetScissor(JNIEnv* env, jclass cls, jlong ctx) {
    nvgResetScissor((NVGcontext*)(uintptr_t)ctx);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgFontSize(JNIEnv* env, jclass cls, jlong ctx, jfloat size) {
    nvgFontSize((NVGcontext*)(uintptr_t)ctx, size);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgFontBlur(JNIEnv* env, jclass cls, jlong ctx, jfloat blur) {
    nvgFontBlur((NVGcontext*)(uintptr_t)ctx, blur);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgFontFace(JNIEnv* env, jclass cls, jlong ctx, jstring name) {
    const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
    nvgFontFace((NVGcontext*)(uintptr_t)ctx, cname);
    (*env)->ReleaseStringUTFChars(env, name, cname);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgTextAlign(JNIEnv* env, jclass cls, jlong ctx, jint align) {
    nvgTextAlign((NVGcontext*)(uintptr_t)ctx, align);
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgTextLetterSpacing(JNIEnv* env, jclass cls, jlong ctx, jfloat spacing) {
    nvgTextLetterSpacing((NVGcontext*)(uintptr_t)ctx, spacing);
}

JNIEXPORT jfloat JNICALL
Java_zeta_client_nanovg_NanoVG_nvgText(JNIEnv* env, jclass cls, jlong ctx, jfloat x, jfloat y, jstring text) {
    const char* ctext = (*env)->GetStringUTFChars(env, text, NULL);
    float advance = nvgText((NVGcontext*)(uintptr_t)ctx, x, y, ctext, NULL);
    (*env)->ReleaseStringUTFChars(env, text, ctext);
    return advance;
}

JNIEXPORT void JNICALL
Java_zeta_client_nanovg_NanoVG_nvgTextBox(JNIEnv* env, jclass cls, jlong ctx, jfloat x, jfloat y, jfloat w, jstring text) {
    const char* ctext = (*env)->GetStringUTFChars(env, text, NULL);
    nvgTextBox((NVGcontext*)(uintptr_t)ctx, x, y, w, ctext, NULL);
    (*env)->ReleaseStringUTFChars(env, text, ctext);
}

JNIEXPORT jfloat JNICALL
Java_zeta_client_nanovg_NanoVG_nvgTextBounds(JNIEnv* env, jclass cls, jlong ctx, jfloat x, jfloat y, jstring text, jfloatArray bounds) {
    const char* ctext = (*env)->GetStringUTFChars(env, text, NULL);
    float b[4];
    float advance = nvgTextBounds((NVGcontext*)(uintptr_t)ctx, x, y, ctext, NULL, b);
    jfloat* jb = (*env)->GetFloatArrayElements(env, bounds, NULL);
    jb[0] = b[0]; jb[1] = b[1]; jb[2] = b[2]; jb[3] = b[3];
    (*env)->ReleaseFloatArrayElements(env, bounds, jb, 0);
    (*env)->ReleaseStringUTFChars(env, text, ctext);
    return advance;
}

JNIEXPORT jint JNICALL
Java_zeta_client_nanovg_NanoVG_nvgCreateFontMem(JNIEnv* env, jclass cls, jlong ctx, jstring name, jobject buf, jint freeOnClose) {
    const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
    unsigned char* data = (unsigned char*)(*env)->GetDirectBufferAddress(env, buf);
    int ndata = (*env)->GetDirectBufferCapacity(env, buf);
    int result = nvgCreateFontMem((NVGcontext*)(uintptr_t)ctx, cname, data, ndata, freeOnClose);
    (*env)->ReleaseStringUTFChars(env, name, cname);
    return result;
}

JNIEXPORT jfloat JNICALL
Java_zeta_client_nanovg_NanoVG_nvgGetTextWidth(JNIEnv* env, jclass cls, jlong ctx, jstring text) {
    const char* ctext = (*env)->GetStringUTFChars(env, text, NULL);
    float b[4];
    float advance = nvgTextBounds((NVGcontext*)(uintptr_t)ctx, 0, 0, ctext, NULL, b);
    (*env)->ReleaseStringUTFChars(env, text, ctext);
    return advance;
}
