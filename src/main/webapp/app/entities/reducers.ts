import contentCreator from 'app/entities/content-creator/content-creator.reducer';
import company from 'app/entities/company/company.reducer';
import artisticContent from 'app/entities/artistic-content/artistic-content.reducer';
import general from 'app/entities/general/general.reducer';
import photography from 'app/entities/photography/photography.reducer';
import paintingOrSculpture from 'app/entities/painting-or-sculpture/painting-or-sculpture.reducer';
import music from 'app/entities/music/music.reducer';
import dance from 'app/entities/dance/dance.reducer';
import cinema from 'app/entities/cinema/cinema.reducer';
import tag from 'app/entities/tag/tag.reducer';
import comment from 'app/entities/comment/comment.reducer';
import notification from 'app/entities/notification/notification.reducer';
import notificationConfiguration from 'app/entities/notification-configuration/notification-configuration.reducer';
import privacyRequest from 'app/entities/privacy-request/privacy-request.reducer';
import project from 'app/entities/project/project.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  contentCreator,
  company,
  artisticContent,
  general,
  photography,
  paintingOrSculpture,
  music,
  dance,
  cinema,
  tag,
  comment,
  notification,
  notificationConfiguration,
  privacyRequest,
  project,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
