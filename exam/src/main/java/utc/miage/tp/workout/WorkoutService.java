package utc.miage.tp.workout;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WorkoutService {

	private final WorkoutRepository workoutRepository;

	public WorkoutService(WorkoutRepository workoutRepository) {
		this.workoutRepository = workoutRepository;
	}

	@Transactional
	public Workout createWorkout(Workout workout) {
		Workout newWorkout = new Workout(workout.getDate(), workout.getDistance(), workout.getDuration());

		Workout savedWorkout = workoutRepository.save(newWorkout);

		return workoutRepository.save(savedWorkout);
	}

	@Transactional(readOnly = true)
	public List<Workout> getAllStatuts() {
		return workoutRepository.findAll();
	}
}
