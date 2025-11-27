package example.note;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для класса {@link NoteLogic}
 */
public class NoteLogicTest {

    private NoteLogic noteLogic;

    /**
     * Создание нового инстанса {@link NoteLogic} перед каждым тестом
     */
    @BeforeEach
    void setUp() {
        noteLogic = new NoteLogic();
    }

    /**
     * Тестируем добавление новой заметки
     */
    @Test
    void addNewNoteShouldNoteBeAdded() {
        String message = noteLogic.handleMessage("/add newnote");
        assertEquals("Note added!", message);
        assertEquals("""
                        Your notes:
                        newnote
                        """,
                noteLogic.handleMessage("/notes"));
    }

    /**
     * Тестируем вывод всех заметок в коректном порядке
     */
    @Test
    void getAllNotesShouldShowAllNotes() {
        noteLogic.handleMessage("/add newnote");
        noteLogic.handleMessage("/add anothernote");
        String notes = noteLogic.handleMessage("/notes");
        assertEquals("""
                        Your notes:
                        newnote
                        anothernote
                        """,
                notes);
    }

    /**
     * Тестируем удаление заметки
     */
    @Test
    void deleteNoteShouldShowAllNotes() {
        noteLogic.handleMessage("/add newnote");
        noteLogic.handleMessage("/add anothernote");
        String message = noteLogic.handleMessage("/delete 1");
        assertEquals("Note deleted!", message);
        assertEquals("""
                        Your notes:
                        anothernote
                        """,
                noteLogic.handleMessage("/notes"));
    }

    /**
     * Тестируем редактирование заметки
     */
    @Test
    void editNoteShouldNoteBeEdited() {
        noteLogic.handleMessage("/add newnote");
        String message = noteLogic.handleMessage("/edit 1 editednote");
        assertEquals("Note edited!", message);
        assertEquals("""
                        Your notes:
                        editednote
                        """,
                noteLogic.handleMessage("/notes"));
    }
}
