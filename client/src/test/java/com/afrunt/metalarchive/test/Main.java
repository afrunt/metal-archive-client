package com.afrunt.metalarchive.test;

import com.afrunt.metalarchive.api.MetalArchiveClient;
import com.afrunt.metalarchive.impl.MetalArchiveClientImpl;
import com.afrunt.metalarchive.model.BandInfo;
import com.afrunt.metalarchive.model.BandKey;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

/**
 * @author Andrii Frunt
 */
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        MetalArchiveClient client = new MetalArchiveClientImpl();
        ForkJoinPool pool = new ForkJoinPool(100);

        long start = System.currentTimeMillis();
        //List<BandKey> bandKeys = pool.submit(() -> client.bandKeysStream().parallel().peek(Main::logBandKey).collect(Collectors.toList())).get();
        //List<BandInfo> bandInfoList = client.bandInfoStream().collect(Collectors.toList());
        //List<BandInfo> bandInfoList = pool.submit(() -> client.bandInfoForLetterStream("~").parallel().peek(Main::logBandInfo).collect(Collectors.toList())).get();
        List<BandInfo> bandInfoList = pool.submit(() -> client.bandInfoStream().parallel().peek(Main::logBandInfo).collect(Collectors.toList())).get();
        long count = bandInfoList.size();
        //long count = pool.submit(() -> client.bandInfoStream().parallel().peek(Main::logBandInfo).count()).get();
        //long count = client.bandInfoForLetterStream("~").parallel().peek(Main::logBandInfo).count();
        LOGGER.info("All {} bandInfo's fetched in {}ms", count, (System.currentTimeMillis() - start));
        FileOutputStream os = new FileOutputStream("band-info.json");
        os.write(new GsonBuilder().setPrettyPrinting().create().toJson(bandInfoList).getBytes());
        os.flush();
        os.close();
    }

    private static void logBandKey(BandKey bandKey) {
        LOGGER.debug("Band key. {} / {} / {} / {}", bandKey.getId(), bandKey.getName(), bandKey.getGenre(), bandKey.getCountry());
    }

    private static void logBandInfo(BandInfo bandInfo) {
        LOGGER.debug("{}Band Info. {} / {} / {} / {} / {} / {} / {} albums / {}",
                bandInfo.getDescription().equals("") ? "EMPTY_DESCRIPTION " : "",
                bandInfo.getKey().getId(),
                bandInfo.getKey().getName(),
                bandInfo.getKey().getGenre(),
                bandInfo.getKey().getCountry(),
                bandInfo.getFormedIn(),
                bandInfo.getLocation(),
                bandInfo.getDiscography().size(),
                bandInfo.getKey().buildUrl()
        );

        bandInfo.getDiscography().forEach(albumInfo ->
                {
                    if (albumInfo.getTrackList().isEmpty()) {
                        LOGGER.info("EMPTY_TRACKLIST -> {}", albumInfo.getKey().buildAlbumUrl());
                    }
                }
        );
    }


}
