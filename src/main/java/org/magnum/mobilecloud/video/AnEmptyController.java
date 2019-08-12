/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.repository.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Set;

@RestController
public class AnEmptyController {

	@Autowired
	private VideoRepository repository;

	public AnEmptyController() {}

	/**
	 * You will need to create one or more Spring controllers to fulfill the
	 * requirements of the assignment. If you use this file, please rename it
	 * to something other than "AnEmptyController"
	 * 
	 * 
		 ________  ________  ________  ________          ___       ___  ___  ________  ___  __       
		|\   ____\|\   __  \|\   __  \|\   ___ \        |\  \     |\  \|\  \|\   ____\|\  \|\  \     
		\ \  \___|\ \  \|\  \ \  \|\  \ \  \_|\ \       \ \  \    \ \  \\\  \ \  \___|\ \  \/  /|_   
		 \ \  \  __\ \  \\\  \ \  \\\  \ \  \ \\ \       \ \  \    \ \  \\\  \ \  \    \ \   ___  \  
		  \ \  \|\  \ \  \\\  \ \  \\\  \ \  \_\\ \       \ \  \____\ \  \\\  \ \  \____\ \  \\ \  \ 
		   \ \_______\ \_______\ \_______\ \_______\       \ \_______\ \_______\ \_______\ \__\\ \__\
		    \|_______|\|_______|\|_______|\|_______|        \|_______|\|_______|\|_______|\|__| \|__|
                                                                                                                                                                                                                                                                        
	 * 
	 */
	
	@RequestMapping(value="/go",method=RequestMethod.GET)
	public @ResponseBody String goodLuck(){
		return "Good Luck!";
	}


	@RequestMapping(value = "video", method = RequestMethod.GET, produces={"application/json"})
	public Collection<Video> getVideo() {
		return repository.findAll();
	}

	@RequestMapping(value = "video/search/findByName", method = RequestMethod.GET, produces={"application/json"})
	public Collection<Video> getVideoByName(@RequestParam String title) {
		return repository.findAllByName(title);
	}

	@RequestMapping(value = "video/search/findByDurationLessThan", method = RequestMethod.GET, produces={"application/json"})
	public Collection<Video> getVideoByDuration(@RequestParam long duration) {
		return repository.findByDurationLessThan(duration);
	}

	@RequestMapping(value = "video/{id}", method = RequestMethod.GET, produces={"application/json"})
	public Video getVideo(@PathVariable Long id) {
		return repository.findById(id);
	}


	@RequestMapping(value = "video", method = RequestMethod.POST, produces={"application/json"})
	public Video createVideo(@RequestBody Video video) {
		repository.save(video);

		return video;
	}

	@RequestMapping(value = "video/{id}/like", method = RequestMethod.POST, produces={"application/json"})
	public ResponseEntity likeVideo(
			@RequestHeader(value="Authorization") String token,
			@PathVariable Long id) {
		Video video = repository.findById(id);

		if (video == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		Set<String> likedBy = video.getLikedBy();
		if (likedBy.contains(token)) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}

		video.setLikes(video.getLikes() + 1);
		likedBy.add(token);
		video.setLikedBy(likedBy);
		repository.save(video);

		return new ResponseEntity(HttpStatus.OK);
	}

	@RequestMapping(value = "video/{id}/unlike", method = RequestMethod.POST, produces={"application/json"})
	public ResponseEntity unlikeVideo(
			@RequestHeader(value="Authorization") String token,
			@PathVariable Long id) {
		Video video = repository.findById(id);

		if (video == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		video.setLikes(video.getLikes() - 1);
		repository.save(video);

		return new ResponseEntity(HttpStatus.OK);
	}
}
