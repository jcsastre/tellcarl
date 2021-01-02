package com.tellcarl.repository;

import com.tellcarl.domain.Symbol;
import com.tellcarl.domain.SymbolType;
import org.springframework.stereotype.Repository;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class SymbolRepository
{
    public Optional<Symbol> findByLemma( String lemma )
    {
        return Optional.ofNullable(symbolsES.get(lemma));
    }

    Map<String, Symbol> symbolsES = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("agua", new Symbol("agua", SymbolType.OTHER, true, "El agua es un símbolo de la vida del soñador.", "water.png")),
            new AbstractMap.SimpleEntry<>("aire", new Symbol("aire", SymbolType.OTHER, true, "El aire es un símbolo de vida.", null))
    );
}
