/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer2.transformer.mh;

import static com.google.android.exoplayer2.transformer.AndroidTestUtil.MP4_ASSET_WITH_INCREASING_TIMESTAMPS_URI_STRING;

import android.content.Context;
import android.graphics.Matrix;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.google.android.exoplayer2.transformer.TransformationRequest;
import com.google.android.exoplayer2.transformer.Transformer;
import com.google.android.exoplayer2.transformer.TransformerAndroidTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/** {@link Transformer} instrumentation test for setting a transformation matrix. */
@RunWith(AndroidJUnit4.class)
public class SetTransformationMatrixTransformationTest {
  @Test
  public void setTransformationMatrixTransform() throws Exception {
    Context context = ApplicationProvider.getApplicationContext();
    Matrix transformationMatrix = new Matrix();
    transformationMatrix.postTranslate(/* dx= */ .2f, /* dy= */ .1f);
    Transformer transformer =
        new Transformer.Builder(context)
            .setTransformationRequest(
                new TransformationRequest.Builder()
                    .setTransformationMatrix(transformationMatrix)
                    .build())
            .build();

    new TransformerAndroidTestRunner.Builder(context, transformer)
        .build()
        .run(
            /* testId= */ "setTransformationMatrixTransform",
            MP4_ASSET_WITH_INCREASING_TIMESTAMPS_URI_STRING);
  }
}