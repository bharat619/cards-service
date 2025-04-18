package com.eazybytes.cards.service.impl;

import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardDto;
import com.eazybytes.cards.entity.Cards;
import com.eazybytes.cards.exception.CardAlreadyExistsException;
import com.eazybytes.cards.exception.ResourceNotFound;
import com.eazybytes.cards.mapper.CardMapper;
import com.eazybytes.cards.repository.CardRepository;
import com.eazybytes.cards.service.ICardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardServiceImpl implements ICardService {
    private CardRepository cardRepository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> card = cardRepository.findByMobileNumber(mobileNumber);
        if(card.isPresent()) {
            throw new CardAlreadyExistsException("Card Already Exists");
        }

        cardRepository.save(createNewCard(mobileNumber));
    }

    @Override
    public CardDto fetchCard(String mobileNumber) {
        Cards card = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFound("Card", "mobileNumber", mobileNumber)
        );

        return CardMapper.mapToCardDto(card, new CardDto());
    }

    @Override
    public boolean updateCard(CardDto cardsDto) {
        Cards card = cardRepository.findByCardNumber(cardsDto.getCardNumber())
                .orElseThrow(() -> new ResourceNotFound("Card", "cardNumber", cardsDto.getCardNumber()));

        CardMapper.mapToCards(cardsDto, card);
        cardRepository.save(card);
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards card = cardRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFound("Card", "mobileNumber", mobileNumber));
        cardRepository.deleteById(card.getCardId());
        return true;
    }

    private Cards createNewCard(String mobileNumber) {
        Cards card = new Cards();
        card.setMobileNumber(mobileNumber);
        Long randomCardNumber = 100000000000L + new Random().nextInt(900000000);

        card.setCardNumber(randomCardNumber.toString());
        card.setCardType(CardsConstants.CREDIT_CARD);
        card.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        card.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        card.setAmountUsed(0);
        return card;
    }
}
