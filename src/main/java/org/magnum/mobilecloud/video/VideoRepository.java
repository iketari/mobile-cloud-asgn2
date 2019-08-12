package org.magnum.mobilecloud.video;

import org.magnum.mobilecloud.video.repository.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

interface VideoRepository extends JpaRepository<Video, Long> {
    Video findById(long id);

    Collection<Video> findAllByName(String name);

    Collection<Video> findByDurationLessThan(long duration);
}