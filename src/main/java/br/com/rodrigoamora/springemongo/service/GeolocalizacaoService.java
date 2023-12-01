package br.com.rodrigoamora.springemongo.service;

import br.com.rodrigoamora.springemongo.model.Contato;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class GeolocalizacaoService {
	
	@Autowired
	private Environment env;
	
	public List<Double> obterLatELongPor(Contato contato) throws ApiException, InterruptedException, IOException{
		String GeoAPI_ApiKey = env.getProperty("geoapi_apikey");
		
		GeoApiContext context = new GeoApiContext().setApiKey(GeoAPI_ApiKey);
		
		GeocodingApiRequest request = GeocodingApi.newRequest(context).address(contato.getEndereco());
		
		GeocodingResult[] results = request.await();
		
		GeocodingResult resultado = results[0];
		
		Geometry geometry = resultado.geometry;
		
		LatLng location = geometry.location;
		
		return Arrays.asList(location.lat, location.lng);
	}

}
