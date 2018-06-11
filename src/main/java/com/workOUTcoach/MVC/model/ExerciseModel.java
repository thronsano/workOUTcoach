package com.workOUTcoach.MVC.model;

import com.workOUTcoach.entity.Exercise;
import com.workOUTcoach.entity.Scheme;
import com.workOUTcoach.utility.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExerciseModel {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private SchemeModel schemeModel;

    public List<Exercise> getExerciseListBySchemeId(int schemeID) {
        List<Exercise> exercises = null;
        Session session = sessionFactory.openSession();

        try {
            session.beginTransaction();
            Query query = session.createQuery("from Exercise as e where e.scheme.id =:schemeId");
            query.setParameter("schemeId", schemeID);

            exercises = query.list();
        } catch (Exception e) {
            Logger.logError("Exception during access to list of schemes");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
        return exercises;
    }

    public void deleteExercise(int exerciseID) throws Exception {
        Session session = sessionFactory.openSession();
        Exercise exercise = getExerciseById(exerciseID);
        try {
            session.beginTransaction();
            session.delete(exercise);
        } catch (Exception e) {
            throw new Exception("No exercise to delete!");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public Exercise getExerciseById(int exerciseID) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            Query query = session.createQuery("from Exercise where id =:exerciseId");
            query.setParameter("exerciseId", exerciseID);

            return (Exercise) query.uniqueResult();

        } catch (Exception e) {
            Logger.logError("Exception during searching for exercise");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
        return null;
    }

    public void addNewExercise(int schemeID, String name, int repetitions) throws Exception {

        Scheme scheme = schemeModel.getSchemeById(schemeID);
        if ((name != null && name.length() > 0) && repetitions > 0) {
            Exercise exercise = new Exercise(name, repetitions, scheme);
            saveExercise(exercise);
        } else {
            throw new Exception("Bad data!");
        }
    }

    private void saveExercise(Exercise exercise) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            session.save(exercise);
        } catch (Exception e) {
            Logger.logError("Exception during adding new exercise");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }

    public void editExerciseById(int exerciseID, String name, int repetitions) throws Exception {

        Exercise exercise = getExerciseById(exerciseID);
        if (exercise != null) {
            if ((name != null && name.length() > 0) && repetitions > 0) {
                exercise.setName(name);
                exercise.setRepetitions(repetitions);
                editExercise(exercise);
            }else{
                throw new Exception("Bad data!");
            }
        }else{
            throw new Exception("Bad data!");
        }
    }

    private void editExercise(Exercise exercise) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Exercise editExercise = session.get(Exercise.class, exercise.getId());
            editExercise.setName(exercise.getName());
            editExercise.setRepetitions(exercise.getRepetitions());
        } catch (Exception e) {
            Logger.logError("Exception during editing exercise");
        } finally {
            session.getTransaction().commit();
            session.close();
        }
    }
}
