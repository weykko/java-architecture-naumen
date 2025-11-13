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
    void addNewNote_ShouldNoteBeAdded() {
        String message = noteLogic.handleMessage("/add new_note");
        assertEquals("Note added!", message);
        assertEquals("""
                        Your notes:
                        new_note
                        """,
                noteLogic.handleMessage("/notes"));
    }

    /**
     * Тестируем вывод всех заметок в коректном порядке
     */
    @Test
    void getAllNotes_ShouldShowAllNotes() {
        noteLogic.handleMessage("/add new_note");
        noteLogic.handleMessage("/add another_note");
        String notes = noteLogic.handleMessage("/notes");
        assertEquals("""
                        Your notes:
                        new_note
                        another_note
                        """,
                notes);
    }

    /**
     * Тестируем удаление заметки
     */
    @Test
    void deleteNote_ShouldShowAllNotes() {
        noteLogic.handleMessage("/add new_note");
        noteLogic.handleMessage("/add another_note");
        String message = noteLogic.handleMessage("/delete 1");
        assertEquals("Note deleted!", message);
        assertEquals("""
                        Your notes:
                        another_note
                        """,
                noteLogic.handleMessage("/notes"));
    }

    /**
     * Тестируем редактирование заметки
     */
    @Test
    void editNote_ShouldNoteBeEdited() {
        noteLogic.handleMessage("/add new_note");
        String message = noteLogic.handleMessage("/edit 1 edited_note");
        assertEquals("Note edited!", message);
        assertEquals("""
                        Your notes:
                        edited_note
                        """,
                noteLogic.handleMessage("/notes"));
    }
}
