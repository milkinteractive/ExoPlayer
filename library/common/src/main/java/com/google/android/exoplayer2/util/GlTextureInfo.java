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
package com.google.android.exoplayer2.util;

import static com.google.android.exoplayer2.util.Assertions.checkState;

import android.graphics.Gainmap;
import androidx.annotation.Nullable;
import com.google.android.exoplayer2.C;

/**
 * Contains information describing an OpenGL texture.
 *
 * @deprecated com.google.android.exoplayer2 is deprecated. Please migrate to androidx.media3 (which
 *     contains the same ExoPlayer code). See <a
 *     href="https://developer.android.com/guide/topics/media/media3/getting-started/migration-guide">the
 *     migration guide</a> for more details, including a script to help with the migration.
 */
@Deprecated
public final class GlTextureInfo {
  /** A {@link GlTextureInfo} instance with all fields unset. */
  public static final GlTextureInfo UNSET =
      new GlTextureInfo(
          /* texId= */ C.INDEX_UNSET,
          /* fboId= */ C.INDEX_UNSET,
          /* rboId= */ C.INDEX_UNSET,
          /* width= */ C.LENGTH_UNSET,
          /* height= */ C.LENGTH_UNSET);

  /** The OpenGL texture identifier, or {@link C#INDEX_UNSET} if not specified. */
  public final int texId;

  /**
   * Identifier of a framebuffer object associated with the texture, or {@link C#INDEX_UNSET} if not
   * specified.
   */
  public final int fboId;

  /**
   * Identifier of a renderbuffer object attached with the framebuffer, or {@link C#INDEX_UNSET} if
   * not specified.
   */
  public final int rboId;

  /** The width of the texture, in pixels, or {@link C#LENGTH_UNSET} if not specified. */
  public final int width;

  /** The height of the texture, in pixels, or {@link C#LENGTH_UNSET} if not specified. */
  public final int height;

  /** The {@link Gainmap} associated to this texture, or {@code null} if not specified. */
  @Nullable public final Gainmap gainmap;

  /**
   * The OpenGL texture identifier for a gainmap associated to this contents of {@link #texId}, or
   * {@link C#INDEX_UNSET} if not specified.
   */
  public final int gainmapTexId;

  /**
   * Creates a new instance with {@code null} {@link Gainmap} and unspecified {@code gainmapTexId}.
   *
   * @param texId The OpenGL texture identifier, or {@link C#INDEX_UNSET} if not specified.
   * @param fboId Identifier of a framebuffer object associated with the texture, or {@link
   *     C#INDEX_UNSET} if not specified.
   * @param rboId Identifier of a renderbuffer object associated with the texture, or {@link
   *     C#INDEX_UNSET} if not specified.
   * @param width The width of the texture, in pixels, or {@link C#LENGTH_UNSET} if not specified.
   * @param height The height of the texture, in pixels, or {@link C#LENGTH_UNSET} if not specified.
   */
  public GlTextureInfo(int texId, int fboId, int rboId, int width, int height) {
    this.texId = texId;
    this.fboId = fboId;
    this.rboId = rboId;
    this.width = width;
    this.height = height;
    this.gainmap = null;
    this.gainmapTexId = C.INDEX_UNSET;
  }

  /**
   * Creates a new instance.
   *
   * @param texId The OpenGL texture identifier, or {@link C#INDEX_UNSET} if not specified.
   * @param fboId Identifier of a framebuffer object associated with the texture, or {@link
   *     C#INDEX_UNSET} if not specified.
   * @param rboId Identifier of a renderbuffer object associated with the texture, or {@link
   *     C#INDEX_UNSET} if not specified.
   * @param width The width of the texture, in pixels, or {@link C#LENGTH_UNSET} if not specified.
   * @param height The height of the texture, in pixels, or {@link C#LENGTH_UNSET} if not specified.
   * @param gainmap The {@link Gainmap} associated to this texture.
   * @param gainmapTexId The OpenGL texture identifier for a gainmap associated with the contents of
   *     {@link #texId}.
   */
  public GlTextureInfo(
      int texId, int fboId, int rboId, int width, int height, Gainmap gainmap, int gainmapTexId) {
    this.texId = texId;
    this.fboId = fboId;
    this.rboId = rboId;
    this.width = width;
    this.height = height;
    this.gainmap = gainmap;
    checkState(
        gainmapTexId != C.INDEX_UNSET, "If gainmap is non-null, the gainmapTexId must be set");
    this.gainmapTexId = gainmapTexId;
  }

  /** Releases all information associated with this instance. */
  public void release() throws GlUtil.GlException {
    if (texId != C.INDEX_UNSET) {
      GlUtil.deleteTexture(texId);
    }
    if (fboId != C.INDEX_UNSET) {
      GlUtil.deleteFbo(fboId);
    }
    if (rboId != C.INDEX_UNSET) {
      GlUtil.deleteRbo(rboId);
    }
    if (gainmapTexId != C.INDEX_UNSET) {
      GlUtil.deleteTexture(gainmapTexId);
    }
  }
}
