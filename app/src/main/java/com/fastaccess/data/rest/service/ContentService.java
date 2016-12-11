/*
 * Copyright 2015 Henrik Olsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fastaccess.data.rest.service;

import com.fastaccess.data.dao.ContentModel;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ContentService {

    @GET("repos/{owner}/{repo}/readme")
    @Headers("Accept: application/vnd.github.html")
    Observable<String> getReadmeHtml(@Path("owner") String owner, @Path("repo") String repo,
                                     @Query("ref") String ref);

    @GET("repos/{owner}/{repo}/readme")
    Observable<String> getRawReadme(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/contents/{path}")
    Observable<ContentModel> getContents(@Path("owner") String owner, @Path("repo") String repo,
                                         @Path("path") String path, @Query("ref") String ref);

    @GET("repos/{owner}/{repo}/contents/{path}")
    Observable<List<ContentModel>> getDirectoryContents(@Path("owner") String owner, @Path("repo") String repo,
                                                        @Path("path") String path, @Query("ref") String ref);

    @HEAD("repos/{owner}/{repo}/readme")
    Observable<Response<Void>> hasReadme(@Path("owner") String owner, @Path("repo") String repo);
}
