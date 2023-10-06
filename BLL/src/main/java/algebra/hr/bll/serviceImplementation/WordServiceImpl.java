package algebra.hr.bll.serviceImplementation;

import algebra.hr.bll.service.WordService;
import algebra.hr.dal.entity.Word;
import algebra.hr.dal.repository.WordRepository;
import hr.algebra.utils.notFoundErrors.CustomNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WordServiceImpl implements WordService {
    private final WordRepository _wordRepository;
    public WordServiceImpl(WordRepository wordRepository) {
        _wordRepository = wordRepository;
    }

    @Override
    public List<Word> findAll() {
        return _wordRepository.findAll();
    }

    @Override
    public Word findById(int id) {
        Optional<Word> wordOptional = _wordRepository.findById(id);

        if (wordOptional.isEmpty()){
            throw new CustomNotFoundException("Word id not found - " + id);
        }

        return wordOptional.get();
    }

    @Override
    @Transactional
    public Word save(Word word) {
        return _wordRepository.save(word);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        _wordRepository.deleteById(id);
    }
}
