package org.suntao.easyorm.scanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.suntao.easyorm.annotation.courseMapper;
import org.suntao.easyorm.annotation.userMapper;
import org.suntao.easyorm.map.MapStatment;
import org.suntao.easyorm.map.ResultMapConfig;
import org.suntao.easyorm.scan.SimpleScanner;

public class scannerTest {
	static SimpleScanner simpleScanner;

	@Before
	public void beforeTest() {
		simpleScanner = new SimpleScanner();
		List<Class<?>> list = new ArrayList();
		list.add(userMapper.class);
		list.add(courseMapper.class);
		simpleScanner.setDaoClasses(list);
		simpleScanner.scan();
	}

	@Test
	public void testFullScan() {
		System.out.println("-------------Full Test------------");
		Map<String, MapStatment> mapStatments = simpleScanner
				.getScannedMapStatment();
		Map<String, ResultMapConfig<?>> resultMaps = simpleScanner
				.getScanedResultMap();
		for (String k : mapStatments.keySet()) {
			MapStatment currentMapStatment = mapStatments.get(k);
			ResultMapConfig<?> currentResultMap = resultMaps.get(k);
			System.out.println(currentMapStatment.getInfoStr());
			Map<String, Integer> paramLocation = currentMapStatment
					.getParamLocation();
			if (paramLocation != null) {
				for (String ks : paramLocation.keySet()) {
					System.out.println(ks + paramLocation.get(ks));
				}
			}
			System.out.println(currentResultMap.getInfoStr());
		}
		System.out.println("-------------Full Test------------");
	}

	@Test
	public void testMapStatmentScan() {
		List<MapStatment> resultStatments = simpleScanner.scanMapStatment();
		for (MapStatment m : resultStatments) {
			System.out.println(m.getInfoStr());
		}
	}

	@Test
	public void testResultMapScan() {
		List<ResultMapConfig> resultmaps = simpleScanner.scanResultMap();
		for (ResultMapConfig r : resultmaps) {
			System.out.println(r.getInfoStr());
		}
	}
}
