import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'gdpApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'appointment',
    data: { pageTitle: 'gdpApp.appointment.home.title' },
    loadChildren: () => import('./appointment/appointment.routes'),
  },
  {
    path: 'consultation',
    data: { pageTitle: 'gdpApp.consultation.home.title' },
    loadChildren: () => import('./consultation/consultation.routes'),
  },
  {
    path: 'doctor-profile',
    data: { pageTitle: 'gdpApp.doctorProfile.home.title' },
    loadChildren: () => import('./doctor-profile/doctor-profile.routes'),
  },
  {
    path: 'hospitalization',
    data: { pageTitle: 'gdpApp.hospitalization.home.title' },
    loadChildren: () => import('./hospitalization/hospitalization.routes'),
  },
  {
    path: 'lab-test-catalog',
    data: { pageTitle: 'gdpApp.labTestCatalog.home.title' },
    loadChildren: () => import('./lab-test-catalog/lab-test-catalog.routes'),
  },
  {
    path: 'lab-test-result',
    data: { pageTitle: 'gdpApp.labTestResult.home.title' },
    loadChildren: () => import('./lab-test-result/lab-test-result.routes'),
  },
  {
    path: 'medical-document',
    data: { pageTitle: 'gdpApp.medicalDocument.home.title' },
    loadChildren: () => import('./medical-document/medical-document.routes'),
  },
  {
    path: 'medication',
    data: { pageTitle: 'gdpApp.medication.home.title' },
    loadChildren: () => import('./medication/medication.routes'),
  },
  {
    path: 'notification',
    data: { pageTitle: 'gdpApp.notification.home.title' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  {
    path: 'patient',
    data: { pageTitle: 'gdpApp.patient.home.title' },
    loadChildren: () => import('./patient/patient.routes'),
  },
  {
    path: 'prescription',
    data: { pageTitle: 'gdpApp.prescription.home.title' },
    loadChildren: () => import('./prescription/prescription.routes'),
  },
  {
    path: 'prescription-item',
    data: { pageTitle: 'gdpApp.prescriptionItem.home.title' },
    loadChildren: () => import('./prescription-item/prescription-item.routes'),
  },
  {
    path: 'user-configuration',
    data: { pageTitle: 'gdpApp.userConfiguration.home.title' },
    loadChildren: () => import('./user-configuration/user-configuration.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
