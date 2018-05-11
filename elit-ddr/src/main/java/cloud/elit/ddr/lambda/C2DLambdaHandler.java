/*
 * Copyright 2018 Emory University
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
 */

package cloud.elit.ddr.lambda;

import java.io.ByteArrayInputStream;
import java.util.StringJoiner;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import cloud.elit.ddr.constituency.CTReader;
import cloud.elit.ddr.constituency.CTTree;
import cloud.elit.ddr.conversion.C2DConverter;
import cloud.elit.ddr.conversion.EnglishC2DConverter;
import cloud.elit.ddr.util.Language;
import cloud.elit.sdk.structure.Sentence;

public class C2DLambdaHandler implements RequestHandler<C2DLambdaInput, String> {
    private C2DConverter converter;
    
    public C2DLambdaHandler() {
        converter = new EnglishC2DConverter();
    }
    
    @Override
    public String handleRequest(C2DLambdaInput in, Context ctx) {
        CTReader reader = new CTReader(new ByteArrayInputStream(in.getTrees().getBytes()), Language.ENGLISH);
        StringJoiner join = new StringJoiner("\n\n");
        Sentence dTree;
        CTTree cTree;

        while ((cTree = reader.next()) != null) {
            if (in.isNorm()) cTree.normalizeIndices();
            dTree = converter.toDependencyGraph(cTree);
            dTree.setNamedEntities(null);
            join.add(dTree.toTSV());
        }
        
        reader.close();
        return join.toString();
    }
}
