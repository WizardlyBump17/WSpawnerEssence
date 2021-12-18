package com.wizardlybump17.wspawneressence.command.reader;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wspawneressence.api.EssenceTier;

public class EssenceTierArgsReader extends ArgsReader<EssenceTier> {

    @Override
    public Class<EssenceTier> getType() {
        return EssenceTier.class;
    }

    @Override
    public EssenceTier read(String s) {
        try {
            return EssenceTier.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
