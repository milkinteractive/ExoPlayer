/*
 * Copyright 2022 The Android Open Source Project
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
package androidx.media3.transformer.mh;

import static androidx.media3.common.util.Assertions.checkNotNull;
import static androidx.media3.transformer.AndroidTestUtil.MP4_ASSET_1080P_4_SECOND_HDR10;
import static androidx.media3.transformer.mh.analysis.FileTestUtil.assertFileHasColorTransfer;
import static com.google.common.truth.Truth.assertThat;

import android.content.Context;
import android.net.Uri;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.Util;
import androidx.media3.transformer.TransformationException;
import androidx.media3.transformer.TransformationRequest;
import androidx.media3.transformer.TransformationTestResult;
import androidx.media3.transformer.Transformer;
import androidx.media3.transformer.TransformerAndroidTestRunner;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

// TODO(b/239172735): Add HLG tests after finding a shareable HLG file.
/** {@link Transformer} instrumentation test for applying an HDR to SDR tone mapping edit. */
@RunWith(AndroidJUnit4.class)
public class SetHdrToSdrToneMapTransformationTest {
  public static final String TAG = "SetHdrToSdrToneMapTransformationTest";

  @Test
  public void transform_toneMapNoRequestedTranscode_hdr10File_toneMapsOrThrows() throws Exception {
    String testId = "transform_toneMapNoRequestedTranscode_hdr10File_toneMapsOrThrows";
    Context context = ApplicationProvider.getApplicationContext();

    Transformer transformer =
        new Transformer.Builder(context)
            .setTransformationRequest(
                new TransformationRequest.Builder().setEnableRequestSdrToneMapping(true).build())
            .addListener(
                new Transformer.Listener() {
                  @Override
                  public void onFallbackApplied(
                      MediaItem inputMediaItem,
                      TransformationRequest originalTransformationRequest,
                      TransformationRequest fallbackTransformationRequest) {
                    // Tone mapping flag shouldn't change in fallback when tone mapping is
                    // requested.
                    assertThat(originalTransformationRequest.enableRequestSdrToneMapping)
                        .isEqualTo(fallbackTransformationRequest.enableRequestSdrToneMapping);
                  }
                })
            .build();

    try {
      TransformationTestResult transformationTestResult =
          new TransformerAndroidTestRunner.Builder(context, transformer)
              .build()
              .run(testId, MediaItem.fromUri(Uri.parse(MP4_ASSET_1080P_4_SECOND_HDR10)));
      Log.i(TAG, "Tone mapped.");
      assertFileHasColorTransfer(transformationTestResult.filePath, C.COLOR_TRANSFER_SDR);
      return;
    } catch (TransformationException exception) {
      Log.i(TAG, checkNotNull(exception.getCause()).toString());
      assertThat(exception).hasCauseThat().isInstanceOf(IllegalArgumentException.class);
      if (Util.SDK_INT < 31) {
        assertThat(exception.errorCode)
            .isEqualTo(TransformationException.ERROR_CODE_HDR_EDITING_UNSUPPORTED);
        assertThat(exception)
            .hasCauseThat()
            .hasMessageThat()
            .isEqualTo("HDR editing and tone mapping not supported under API 31.");
      } else {
        assertThat(exception.errorCode)
            .isEqualTo(TransformationException.ERROR_CODE_DECODING_FORMAT_UNSUPPORTED);
        assertThat(exception)
            .hasCauseThat()
            .hasMessageThat()
            .isEqualTo("Tone-mapping requested but not supported by the decoder.");
      }
      return;
    }
  }

  @Test
  public void transform_toneMapAndTranscode_hdr10File_toneMapsOrThrows() throws Exception {
    String testId = "transform_toneMapAndTranscode_hdr10File_toneMapsOrThrows";
    Context context = ApplicationProvider.getApplicationContext();

    Transformer transformer =
        new Transformer.Builder(context)
            .setTransformationRequest(
                new TransformationRequest.Builder()
                    .setEnableRequestSdrToneMapping(true)
                    .setRotationDegrees(180)
                    .build())
            .addListener(
                new Transformer.Listener() {
                  @Override
                  public void onFallbackApplied(
                      MediaItem inputMediaItem,
                      TransformationRequest originalTransformationRequest,
                      TransformationRequest fallbackTransformationRequest) {
                    // Tone mapping flag shouldn't change in fallback when tone mapping is
                    // requested.
                    assertThat(originalTransformationRequest.enableRequestSdrToneMapping)
                        .isEqualTo(fallbackTransformationRequest.enableRequestSdrToneMapping);
                  }
                })
            .build();

    try {
      TransformationTestResult transformationTestResult =
          new TransformerAndroidTestRunner.Builder(context, transformer)
              .build()
              .run(testId, MediaItem.fromUri(Uri.parse(MP4_ASSET_1080P_4_SECOND_HDR10)));
      Log.i(TAG, "Tone mapped.");
      assertFileHasColorTransfer(transformationTestResult.filePath, C.COLOR_TRANSFER_SDR);
      return;
    } catch (TransformationException exception) {
      Log.i(TAG, checkNotNull(exception.getCause()).toString());
      assertThat(exception).hasCauseThat().isInstanceOf(IllegalArgumentException.class);
      if (Util.SDK_INT < 31) {
        assertThat(exception.errorCode)
            .isEqualTo(TransformationException.ERROR_CODE_HDR_EDITING_UNSUPPORTED);
        assertThat(exception)
            .hasCauseThat()
            .hasMessageThat()
            .isEqualTo("HDR editing and tone mapping not supported under API 31.");
      } else {
        assertThat(exception.errorCode)
            .isEqualTo(TransformationException.ERROR_CODE_DECODING_FORMAT_UNSUPPORTED);
        assertThat(exception)
            .hasCauseThat()
            .hasMessageThat()
            .isEqualTo("Tone-mapping requested but not supported by the decoder.");
      }
      return;
    }
  }
}
