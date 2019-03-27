package com.a4tech.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.a4tech.shipping.iservice.IShippingOrder;
import com.a4tech.shipping.model.ChannelConfiguration;

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
	  
}
