package com.a4tech.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a4tech.shipping.iservice.IShippingOrder;
import com.a4tech.shipping.model.ChannelConfiguration;
import com.a4tech.shipping.model.DistrictClubOrdByPass;

@Service
public class OrderService {
	@Autowired
	private IShippingOrder shippingOrder;
  
	  public List<ChannelConfiguration> getChannelsList(String channelSequence){
		 List<ChannelConfiguration> allChannels = shippingOrder.getAllChannelConfigurations();
		  List<String> channelSeq = Arrays.asList(channelSequence.split(","));
		return allChannels.stream().filter(channel -> channelSeq.contains(channel.getSequence()))
				.collect(Collectors.toList());
	  }
	  public boolean isDistrictByPass(List<DistrictClubOrdByPass> distBypassList, String distrciName) {
			for (DistrictClubOrdByPass districtClubOrdByPass : distBypassList) {
				if (districtClubOrdByPass.getDistrictName().equalsIgnoreCase(distrciName)) {
					if (isDistrictOrderByPassDate(districtClubOrdByPass.getStartDate(),
							districtClubOrdByPass.getEndDate())) {
						return true;
					}

				}
			}
			return false;
		}
		private boolean isDistrictOrderByPassDate(LocalDate startDate, LocalDate endDate) {
			LocalDate now = LocalDate.now();
			if ((now.isAfter(startDate) && now.isBefore(endDate)) || (now.equals(startDate) || now.equals(endDate))) {
				return true;
			}
			return false;
		}
	
}
