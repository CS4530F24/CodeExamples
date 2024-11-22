#include <jni.h>
#include <string>
#include <vector>
// Write C++ code here.
//
// Do not forget to dynamically load the C++ library into your application.
//
// For instance,
//
// In MainActivity.java:
//    static {
//       System.loadLibrary("box2ddemo");
//    }
//
// Or, in MainActivity.kt:
//    companion object {
//      init {
//         System.loadLibrary("box2ddemo")
//      }
//    }

#include <box2d/box2d.h>

struct BallData {
    b2Vec2 position;
    float radius;
};

struct B2dState {
    b2World world;
    std::vector<b2Body*> ballBodies;

    B2dState(std::vector<BallData> ballData) : world(b2Vec2(0, -10)) {
        b2BodyDef groundBodyDef;
        groundBodyDef.position.Set(0.0f, -10);

        //TODO: call destructor
        auto groundBody = world.CreateBody(&groundBodyDef);
        b2PolygonShape groundBox;
        groundBox.SetAsBox(50.0f, 10.0f);

        groundBody->CreateFixture(&groundBox, 0.0f);

        //add some walls too
        b2BodyDef leftWallDef;
        leftWallDef.position.Set(-10, 0);
        auto leftWallBody = world.CreateBody(&leftWallDef);
        b2PolygonShape wallBox;
        wallBox.SetAsBox(4.0f, 10.0f);
        leftWallBody->CreateFixture(&wallBox, 0.0f);

        b2BodyDef rightWallDef;
        rightWallDef.position.Set(10, 0);
        auto rightWallBody = world.CreateBody(&rightWallDef);
        rightWallBody->CreateFixture(&wallBox, 0.0f);

        for(const auto& ball : ballData) {
            b2BodyDef bodyDef;
            bodyDef.type = b2_dynamicBody;
            bodyDef.position.Set(ball.position.x, ball.position.y);

            b2Body* body = world.CreateBody(&bodyDef);
            ballBodies.push_back(body);
            b2CircleShape dynamicBall;
            dynamicBall.m_radius = ball.radius;
            b2FixtureDef fixtureDef;
            fixtureDef.shape = &dynamicBall;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = 0.3f;
            fixtureDef.restitution = 0.99f;
            body->CreateFixture(&fixtureDef);
        }
    }
};

extern "C"
JNIEXPORT void JNICALL
Java_com_example_box2ddemo_NativeWrappers_helloWorld(JNIEnv *env, jobject thiz) {
    // Get system class
    jclass syscls = env->FindClass("android/util/Log");
    auto eID = env->GetStaticMethodID(syscls, "e", "(Ljava/lang/String;Ljava/lang/String;)I");
// Invoke the method
    std::string cppstring = "Hello again";// make an array of jchar (UTF-16 unsigned short encoding)
    jstring str = env->NewStringUTF(cppstring.c_str());
    env->CallStaticIntMethod(syscls, eID, str, str);

    b2Vec2 vec(1.0, 2.0);
    b2World world(vec);
    auto mag = vec.Length();
}
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_box2ddemo_NativeWrappers_initBox2D(JNIEnv *env, jobject thiz,
                                                    jobjectArray ballArray) {

    auto numBalls = env->GetArrayLength(ballArray);
    std::vector<BallData> ballData;
    ballData.reserve(numBalls);
    for(auto i = 0; i < numBalls; i++){
        auto ballJObj = env->GetObjectArrayElement(ballArray, i);
        auto x = env->CallFloatMethod(ballJObj,
             env->GetMethodID(env->GetObjectClass(ballJObj), "getX", "()F"));
        auto y = env->CallFloatMethod(ballJObj,
                                      env->GetMethodID(env->GetObjectClass(ballJObj), "getY", "()F"));
        auto radius = env->CallFloatMethod(ballJObj,
                                      env->GetMethodID(env->GetObjectClass(ballJObj), "getRadius", "()F"));
        ballData.push_back(BallData{b2Vec2(x,y), radius});
    }

    auto state = new B2dState(ballData);
    return reinterpret_cast<uintptr_t>(state);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_box2ddemo_NativeWrappers_step(JNIEnv *env, jobject thiz, jlong ptr,
                                               jobjectArray balls) {
    auto state = reinterpret_cast<B2dState*>(ptr);
    const auto timestep = 1.0f / 60.0f;
    int32 velocityIterations = 6;
    int32 positionIterations = 2;
    state->world.Step(timestep, velocityIterations, positionIterations);

    for(auto i = 0; i < state->ballBodies.size(); i++){
        auto body = state->ballBodies[i];
        auto ballJObj = env->GetObjectArrayElement(balls, i);
        auto setXMethod = env->GetMethodID(env->GetObjectClass(ballJObj),
                                           "setX",
                                           "(F)V");
        env->CallVoidMethod(ballJObj, setXMethod, body->GetPosition().x);
        env->CallVoidMethod(ballJObj, env->GetMethodID(env->GetObjectClass(ballJObj), "setY",
                                                       "(F)V"), body->GetPosition().y);
    }
}
extern "C"
JNIEXPORT jobject JNICALL
Java_com_example_box2ddemo_NativeWrappers_newJNIFunction(JNIEnv *env, jobject thiz,
                                                         jobjectArray arr, jstring s,
                                                         jobject point) {
    // TODO: implement newJNIFunction()
}