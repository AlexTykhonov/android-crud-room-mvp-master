package tech.alvarez.contacts.listedit;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import java.util.List;

import tech.alvarez.contacts.data.db.dao.PersonDao;
import tech.alvarez.contacts.data.db.entity.Person;

public class ListPresenter implements ListContract.Presenter {
    // вью
    private final ListContract.View mView;
    // объект дао
    private final PersonDao personDao;

    // конструктор, берет вью и дао
    public ListPresenter(ListContract.View view, PersonDao personDao) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.personDao = personDao;
    }

    @Override
    public void start() {

    }

    @Override
    public void addNewPerson() {
        mView.showAddPerson();
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }
    // метод в персонДао вызываем найти всех персон, затем слушаетель ЛивДата из персон.
    @Override
    public void populatePeople() {
        personDao.findAllPersons().observeForever(new Observer<List<Person>>() {
            @Override
            // если происходит изменение
            public void onChanged(@Nullable List<Person> persons) {
                // во вью передается список персон
                mView.setPersons(persons);
                if (persons == null || persons.size() < 1) {
                    // если список пуст
                    mView.showEmptyMessage();
                }
            }
        });
    }

    // открыть список редактирования
    @Override
    public void openEditScreen(Person person) {
        mView.showEditScreen(person.id);
    }
    // открывает метод во вью подтверждение удаления
    @Override
    public void openConfirmDeleteDialog(Person person) {
        mView.showDeleteConfirmDialog(person);
    }
    // удаление персоны
    @Override
    public void delete(long personId) {
        Person person = personDao.findPerson(personId);
        personDao.deletePerson(person);
    }
}
